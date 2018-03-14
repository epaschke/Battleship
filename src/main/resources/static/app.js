var direct = "http://localhost:8080/"
var playerId;
var state = {
  currentShip: null,
  count: 0,
  start: [],
  end: []
};
var interval;

$(document).ready(function(){

 //when user clicks the JOIN button
  $("#joinGame").click(function() {
    $.ajax({url: direct + 'joinGame'})
     .then(function(resp){
       if (resp.success) { //if there is room in the game for another player
         //all hidden divs are shown
         $("#player").text(resp.player);
         playerId = () => parseInt($("#player").text());
         $("#info").css("display", "flex");
         $("#joinGame").hide();
         $(".buttons").show();
         $("#board-container").show();
         $("#prompt").show();
         $("#prompt").text("To place ships of 5 different lengths, click on a button below.");
         //interval is set to ping backend for changes every 3 seconds to update game board
         interval = setInterval(getChanges, 3000);
       } else {
         $("#prompt").text(resp.errorMessage);
       }
     });
   });

   //when user clicks EXIT button
   $("#leaveGame").click(function(){
    $.ajax({url: direct + 'leaveGame'})
    .then(function(resp){
      if (resp.success) {
        $("#prompt").text("You have left the game. Refresh page to start new game.");
        $("#info").hide();
        $(".buttons").hide();
        $("#board-container").hide();
        clearInterval(interval);
      }
    });
  });

//when user clicks on a ship button
  $(".ship-button").click(function(event){
    $(this).css("background-color", "red"); //set button color to red
    state.currentShip = this.id; //set current ship to button id
    $("#prompt").text(`Select a start and end for a ship of length ${state.currentShip.split("ship")[1]}!`)
  });

  //when user clicks on a cell of the Player Board
  $(".PlayerBoardCell").click( function(event){
    //if no ship button has been selected
    if (!state.currentShip){
      $("#prompt").text("Select a ship!");
     //if cell is the first cell clicked for this ship
    } else if (!state.count){
      //if cell already has a ship
      if ($(this).css("background-color") !== "rgb(255, 165, 0)") {
        state.shipLength = state.currentShip.split("ship")[1];
        state.startId = this.id;
        state.start = this.id.split("");
        state.start.shift();
        $(this).css("background-color", "orange");
        state.count++;
        $("#prompt").text(`Start selected for ship ${state.shipLength}. Now select endpoint.`)
      } else {
        $("#prompt").text("You already placed a ship there.")
      }
    //if cell is second cell clicked for this ship
    } else {
      state.end = this.id.split("");
      state.end.shift();
      var _this = this;
      $(this).css("background-color", "orange");
      //request backend to verify ship placement
      $.ajax({
        "url": direct + `setShip/${playerId()}`,
        method: 'POST',
        headers: {
          "content-type": "application/json",
          "cache-control": "no-cache"
        },
        data: JSON.stringify({ "shipLength": parseInt(state.shipLength), "start1": parseInt(state.start[0]), "start2": parseInt(state.start[1]), "end1": parseInt(state.end[0]), "end2": parseInt(state.end[1]) })
      }).done(function(resp){
        // if ship placement is valid, fill in orange squares between start and end
        if (resp.success) {
            // if ship is horizontal
            if (state.start[0] === state.end[0]){
                let lesser = state.start[1] > state.end[1] ? state.end[1] : state.start[1];
                let greater = state.start[1] < state.end[1] ? state.end[1] : state.start[1];
                for (let i = lesser; i < greater; i++){
                    $(`#p${state.start[0]}${i}`).css("background-color", "orange");
                }
            //if ship is vertical
            } else {
                let lesser = state.start[0] > state.end[0] ? state.end[0] : state.start[0];
                let greater = state.start[0] < state.end[0] ? state.end[0] : state.start[0];
                for (let i = lesser; i < greater; i++){
                    $(`#p${i}${state.start[1]}`).css("background-color", "orange");
                }
            }
          $("#prompt").text("Ship was placed!")
        // if ship placement is invalid, change squares back to white
        } else {
          $(`#${state.startId}`).css("background-color", "white");
          $(_this).css("background-color", "white");
          $(`#${state.currentShip}`).css("background-color", "white");
          $("#prompt").text(resp.errorMessage);
        }
        //reset ship state
        state.currentShip = null;
        state.count = 0;
      })
      .catch(function(error){
        console.log(error);
      })
    }
  });

  //when user clicks cell on attack board
  $(".AttackBoardCell").click(function(event){
      var x = this.id.split("");
      var _this = this;
    //verify move with backend
    $.ajax({
        "url": direct + `move/${playerId()}`,
        method: 'POST',
        headers: {
          "content-type": "application/json",
          "cache-control": "no-cache"
        },
        data: JSON.stringify({ "row": x[0], "column": x[1] })
    }).done(function(resp){
      //move is valid, change color based on whether it is a hit or miss
      if (resp.success){
        var color = resp.correctHit ? 'green' : 'red';
        $(_this).css("background-color", color);
        var text = resp.correctHit ? "Good hit!" : "Miss!";
        $("#prompt").text(text);
      //move is invalid
      } else {
        $("#prompt").text(resp.errorMessage);
      }
    })
    .catch(function(error){
      console.log(error);
    })
  });

  //polls backend for changes to player board state
  function getChanges() {
    $.ajax({"url": direct + `getChanges/${playerId()}`})
    .done(function(resp){
      if (resp.success){
        //if game is started
        if (resp.whoseTurn !== 0) {
        //if cell was hit, change color
          for (let rowId = 0; rowId < 10; rowId++){
            resp.board[`r${rowId}`].map((cell, cellId) => {
                  if (cell === "x") {
                    $(`#p${rowId}${cellId}`).css("background-color", "green");
                  } else if (cell === "o") {
                    $(`#p${rowId}${cellId}`).css("background-color", "lightgray");
                  }
            });
          }
          //update prompt based on player
          $("#prompt").text(`It's ${resp.whoseTurn === playerId() ? "your turn. Click anywhere on the attack board." : "their turn!"}`)
          //if game is over
          if (resp.gameOver){
             $("#prompt").text(`Game is over. ${resp.winner === playerId() ? "You won!" : "You lost!"}`);
             clearInterval(interval);
           }
        }
      }
    })
    .catch(function(error){
      console.log(error);
    });
  }

});

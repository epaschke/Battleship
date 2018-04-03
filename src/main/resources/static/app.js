const direct = "http://localhost:8080/"
const state = {
  currentShip: null,
  count: 0,
  start: [],
  end: []
};
const gameFunctions = {};
let interval;

$(document).ready(function(){

 //when user clicks the JOIN button
  $("#joinGame").click(function() {
    $.ajax({url: direct + 'joinGame'})
     .then(function(resp){
       if (resp.success) { //if there is room in the game for another player
         //all hidden divs are shown
         $("#player").text(resp.player);
         gameFunctions.playerId = () => parseInt($("#player").text());
         $("#info").css("display", "flex");
         $("#joinGame").hide();
         $(".buttons").show();
         $("#board-container").show();
         $("#prompt").show().text("To place ships of 5 different lengths, click on a button below.");
         for (let i = 0; i < 10; i++){
           $("#playerBoard").append(`<div id="playerBoard${i}"><div class="label">${i}</div></div>`);
           $("#attackBoard").append(`<div id="attackBoard${i}"><div class="label">${i}</div></div>`);
         }
        for (let i = 0; i < 10; i++){
          for (let j = 0; j < 10; j++){
            $(`#playerBoard${i}`).append(`<div id="p${i}${j}" class="PlayerBoardCell water"></div>`);
            $(`#attackBoard${i}`).append(`<div id="p${i}${j}" class="AttackBoardCell water"></div>`);
          }
        }
         // interval is set to ping backend for changes every 3 seconds to update game board
         interval = setInterval(gameFunctions.getChanges, 3000);
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
  $(".ship-button").click(function (e) {
    $(this).addClass("selected");
    state.currentShip = this.id; //set current ship to button id
    $("#prompt").text(`Select a start and end for a ship of length ${state.currentShip.split("ship")[1]}!`)
  });

  //when user clicks on a cell of the Player Board
  $("#playerBoard").click(".PlayerBoardCell", function (e) {
    //if no ship button has been selected
    if (!state.currentShip){
      $("#prompt").text("Select a ship!");
     //if cell is the first cell clicked for this ship
    } else if ($(e.target).hasClass("water")) {
       if (!state.count){
        state.shipLength = state.currentShip.split("ship")[1];
        state.startId = e.target.id;
        state.start = e.target.id.split("");
        $(e.target).addClass("ship").removeClass("water");
        state.count++;
        if (state.shipLength == 1){
          gameFunctions.attemptSetShip(e.target);
        } else {
          $("#prompt").text(`Start selected for ship ${state.shipLength}. Now select endpoint.`)
        }
    //if cell is second cell clicked for this ship
      } else {
        gameFunctions.attemptSetShip(e.target);
      }
    } else {
        $("#prompt").text("You already placed a ship there.")
      }
  });

  //when user clicks cell on attack board
  $("#attackBoard").click(".AttackBoardCell", function (e){
      let x = e.target.id.split("");
      let _this = e.target;
    //verify move with backend
    $.ajax({
        "url": direct + `move/${gameFunctions.playerId()}`,
        method: 'POST',
        headers: {
          "content-type": "application/json",
          "cache-control": "no-cache"
        },
        data: JSON.stringify({ "row": parseInt(x[1]), "column": parseInt(x[2]) })
    }).done(function(resp){
      //move is valid, change color based on whether it is a hit or miss
      if (resp.success){
        let classToAdd = resp.correctHit ? 'hit' : 'miss';
        $(_this).addClass(classToAdd).removeClass('water');
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

});

// GAME FUNCTIONS

gameFunctions.attemptSetShip = (_this) => {
  state.end = _this.id.split("");
  $(_this).addClass("ship").removeClass("water");
  $.ajax({
    "url": direct + `setShip/${gameFunctions.playerId()}`,
    method: 'POST',
    headers: {
      "content-type": "application/json",
      "cache-control": "no-cache"
    },
    data: JSON.stringify({ "shipLength": parseInt(state.shipLength), "start1": parseInt(state.start[1]), "start2": parseInt(state.start[2]), "end1": parseInt(state.end[1]), "end2": parseInt(state.end[2]) })
  }).done(function(resp){
    if (resp.success) { // if ship placement is valid, fill in ship squares to colors
      gameFunctions.changeShipSquares();
    } else { // if ship placement is invalid, change squares back to water
      $(`#${state.startId}`).addClass("water").removeClass("ship");
      $(_this).addClass("water").removeClass("ship");
      $(`#${state.currentShip}`).removeClass("selected");
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

gameFunctions.changeShipSquares = () => {
  console.log(state);
  // if ship is horizontal
  if (state.start[1] === state.end[1]){
      let lesser = state.start[2] > state.end[2] ? state.end[2] : state.start[2];
      let greater = state.start[2] < state.end[2] ? state.end[2] : state.start[2];
      for (let i = lesser; i < greater; i++){
          $(`#p${state.start[1]}${i}`).addClass("ship").removeClass("water");
      }
  //if ship is vertical
  } else {
      let lesser = state.start[1] > state.end[1] ? state.end[1] : state.start[1];
      let greater = state.start[1] < state.end[1] ? state.end[1] : state.start[1];
      for (let i = lesser; i < greater; i++){
          $(`#p${i}${state.start[2]}`).addClass("ship").removeClass("water");
      }
  }
  $("#prompt").text("Ship was placed!")
}

  //polls backend for changes to player board state
gameFunctions.getChanges = () => {
  $.ajax({"url": direct + `getChanges/${gameFunctions.playerId()}`})
  .done(function(resp){
    if (resp.success){
      //if game is started
      if (resp.whoseTurn !== 0) {
      //if cell was hit, change color
        for (let rowId = 0; rowId < 10; rowId++){
          resp.board[`r${rowId}`].map((cell, cellId) => {
                if (cell === "x") {
                  $(`#p${rowId}${cellId}`).addClass("oppHit").removeClass("water");
                } else if (cell === "o") {
                  $(`#p${rowId}${cellId}`).addClass("oppMiss").removeClass("water");
                }
          });
        }
        //update prompt based on player
        $("#prompt").text(`It's ${resp.whoseTurn === gameFunctions.playerId() ? "your turn. Click anywhere on the attack board." : "their turn!"}`)
        //if game is over
        if (resp.gameOver){
           $("#prompt").text(`Game is over. ${resp.winner === gameFunctions.playerId() ? "You won!" : "You lost!"}`);
           clearInterval(interval);
         }
      }
    }
  })
  .catch(function(error){
    console.log(error);
  });
}

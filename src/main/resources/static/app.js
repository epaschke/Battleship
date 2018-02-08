var direct = "http://localhost:8080/"
var playerId;
var state = {
  currentShip: null,
  count: 0,
  start: []
};
var interval;

$(document).ready(function(){

  $("#joinGame").click(function() {
    $.ajax({url: direct + 'joinGame'})
     .then(function(resp){
       if (resp.success) {
         $("#player").text(resp.player);
         playerId = () => parseInt($("#player").text());
         $("#info").show();
         $("#joinGame").hide();
         $(".buttons").show();
         $("#board-container").show();
         $("#prompt").text("To place ships of 5 different lengths, click on a button below.");
         interval = setInterval(getChanges, 3000);
       } else {
         $("#prompt").text(resp.errorMessage);
       }
      })
   });

   $("#leaveGame").click(function(){
    $.ajax({url: direct + 'leaveGame'})
    .then(function(resp){
      if (resp.success) {
        $("#prompt").text("You have left the game.");
        clearInterval(interval);
      }
    })
   })

$(".ship-button").click(function(event){
  $(this).css("background-color", "red");
  state.currentShip = this.id;
  $("#prompt").text("Select a start and end for the ship!")
});

  $(".PlayerBoardCell").click( function(event){
    if (!state.currentShip){
      $("#prompt").text("Select a ship!");
    } else if (!state.count){
      if ($(this).css("background-color") !== "rgb(255, 165, 0)") {
        state.shipLength = state.currentShip.split("ship")[1];
        state.startId = this.id;
        state.start = this.id.split("");
        state.start.shift();
        $(this).css("background-color", "orange");
        state.count++;
        $("#prompt").text("Start selected. Now select endpoint.")
      } else {
        $("#prompt").text("You already placed a ship there.")
      }
    } else {
        var end = this.id.split("");
        end.shift();
        var _this = this;
        $(this).css("background-color", "orange");
        $.ajax({
          "url": direct + `setShip/${playerId()}`,
          method: 'POST',
          headers: {
            "content-type": "application/json",
            "cache-control": "no-cache"
          },
          data: JSON.stringify({ "shipLength": parseInt(state.shipLength), "start1": parseInt(state.start[0]), "start2": parseInt(state.start[1]), "end1": parseInt(end[0]), "end2": parseInt(end[1]) })
      }).done(function(resp){
        if (resp.success) {
          resp.board.map((row, rowId) => {
            row.map((cell, cellId) => {
              if (cell === state.shipLength) {
                $(`#p${rowId}${cellId}`).css("background-color", "orange");
              }
            })
          })
          $("#prompt").text("Ship was placed!")
        } else {
          $(`#${state.startId}`).css("background-color", "white");
          $(_this).css("background-color", "white");
          $(`#${state.currentShip}`).css("background-color", "white");
          $("#prompt").text(resp.errorMessage);
        }
        state.currentShip = null;
        state.count = 0;
      })
      .catch(function(error){
        console.log(error);
      })
    }

  });

$(".AttackBoardCell").click(function(event){
    var x = this.id.split("");
    var _this = this;
  $.ajax({
      "url": direct + `move/${playerId()}`,
      method: 'POST',
      headers: {
        "content-type": "application/json",
        "cache-control": "no-cache"
      },
      data: JSON.stringify({ "row": x[0], "column": x[1] })
  }).done(function(resp){
    console.log(resp);
    if (resp.success){
      var color = resp.correctHit ? 'green' : 'red';
      $(_this).css("background-color", color);
      var text = resp.correctHit ? "Good hit!" : "Miss!";
      $("#prompt").text(text);
    }
  })
  .catch(function(error){
    console.log(error);
  })
});

function getChanges() {
  $.ajax({"url": direct + `getChanges/${playerId()}`})
  .done(function(resp){
    if (resp.success){
      if (resp.whoseTurn !== 0) {
      resp.board.map((row, rowId) => {
        row.map((cell, cellId) => {
          if (cell === "x") {
            $(`#p${rowId}${cellId}`).css("background-color", "green");
          } else if (cell == "o") {
            $(`#p${rowId}${cellId}`).css("background-color", "lightgray");
          }
        })
      })
      $("#prompt").text(`It's ${resp.whoseTurn === playerId() ? "your turn. Click anywhere on the attack board." : "their turn!"}`)
      if (resp.gameOver){
         $("#prompt").text(`Game is over. ${resp.winner === playerId() ? "You won!" : "You lost!"}`);
         clearInterval(interval);
       }
      }
    }
  })
  .catch(function(error){
    console.log(error);
  })
}

})

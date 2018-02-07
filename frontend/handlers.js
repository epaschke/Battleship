var direct = "http://localhost:8080/"
var currentShip = null;
var count = 0;
var StartId;
var start;
var shipLength;
var interval;

$(document).ready(function(){

  $("#joinGame").click(function() {
    $.ajax({url: direct + 'joinGame'})
     .then(function(resp){
       if (resp.success) {
         $("#info").show();
         $("#player").text(resp.player);
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
      }
    })
   })

$(".ship-button").click(function(event){
  $(this).css("background-color", "red");
  currentShip = this.id;
  $("#prompt").text("Select a start and end for the ship!")
});

  $(".PlayerBoardCell").click( function(event){
    if (!currentShip){
      console.log(currentShip);
    } else if (!count){
      if ($(this).css("background-color") !== "rgb(255, 165, 0)") {
        shipLength = currentShip.split("ship")[1];
        startId = this.id;
        start = this.id.split("");
        start.shift();
        $(this).css("background-color", "orange");
        count++;
        $("#prompt").text("Start selected. Now select endpoint.")
      } else {
        $("#prompt").text("You already placed a ship there.")
      }
    } else {
        console.log('else');
        end = this.id.split("");
        end.shift();
        var _this = this;
        $(this).css("background-color", "orange");
        console.log({ "shipLength": parseInt(shipLength), "start1": start[0], "start2": start[1], "end1": end[0], "end2": end[1] });
        $.ajax({
          "url": direct + `setShip/${$("#player").text()}`,
          method: 'POST',
          headers: {
            "content-type": "application/json",
            "cache-control": "no-cache"
          },
          data: JSON.stringify({ "shipLength": parseInt(shipLength), "start1": parseInt(start[0]), "start2": parseInt(start[1]), "end1": parseInt(end[0]), "end2": parseInt(end[1]) })
      }).done(function(resp){
        console.log(resp);
        if (resp.success) {
          resp.board.map((row, rowId) => {
            row.map((cell, cellId) => {
              if (cell === shipLength) {
                $(`#p${rowId}${cellId}`).css("background-color", "orange");
              }
            })
          })
          $("#prompt").text("Ship was placed!")
        } else {
          $(`#${startId}`).css("background-color", "white");
          $(_this).css("background-color", "white");
          $(`#${currentShip}`).css("background-color", "white");
          $("#prompt").text(resp.errorMessage);
        }
        currentShip = null;
        count = 0;
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
      "url": direct + `move/${$("#player").text()}`,
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
      if (resp.gameOver){
        $("#prompt").text("Game is over!");
      }
    }
  })
  .catch(function(error){
    console.log(error);
  })
});

function getChanges() {
  $.ajax({"url": direct + `getChanges/${$("#player").text()}`})
  .done(function(resp){
    console.log(resp);
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
      $("#prompt").text(`It's player ${resp.whoseTurn}'s turn!`)
      if (resp.gameOver){
         $("#prompt").text(`Game is over. Player ${resp.winner} is the winner!`);
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

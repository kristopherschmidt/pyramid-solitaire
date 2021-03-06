	var getCard = function(cardName) {
         var rank = cardName.charAt(0);
         if (rank === 'T') {
        	 rank = '10';
         }
         var suit = cardName.charAt(1);
         return new playingCards.card(rank, playingCards.defaults.ranks[rank], suit, playingCards.defaults.suits[suit]);
	}
	
	var getGameUrl = function() {
		return "/games/"+$('#gameName').val();
	}
	
	
	var isPyramidBottom = function(i, j, board) {
		return i == 6 || (board.pyramid[i+1][j] == null && board.pyramid[i+1][j+1] == null);
	}
    
    var displayPyramid = function(data) {
    	$('#pyramidDisplay').html('');
    	for (var i = 0; i < data.pyramid.length; ++i) {
    		for (var j = 0; j <= i; ++j) {
    			var cardJSON = data.pyramid[i][j];
    			if (cardJSON != null) {
        			var card = getCard(cardJSON.name);
        			var top = 1 + 3*i;
        			var left = 2*(7 - i) + 5*j;
        			var zindex = 10*i;
        			var cardElement = $("<div class='pyramidCard' style='top:"+top+"em;left:"+left+"em;z-index:"+zindex+"'>" 
                			+ card.getHTML() 
                			+ "</div>");
        			$('#pyramidDisplay').append(cardElement);
        			cardElement.data("cardJSON", cardJSON);
        			if (isPyramidBottom(i, j, data)) {
        				cardElement.click(cardClickHandler);
        			}
    			}
    		}
    	}
    }

    var cardClickHandler = function() {
    	var previouslySelected = highlightCard($(this));
    	var thisCard = jQuery.data(this, "cardJSON");
		if (previouslySelected != null || thisCard.value == 13) {
			var cardData = new Object();
			cardData.card1 = thisCard;
			if (previouslySelected != null && thisCard.value != 13) {
				cardData.card2 = jQuery.data(previouslySelected, "cardJSON");
			}
		   $.ajax({
			      type: "POST",
			      contentType: "application/json",
			      url: getGameUrl() + "/moves/removeCards",
			      data: JSON.stringify(cardData),
			      dataType: "json",
			      success: refreshGameBoard
			});
		}
    }
    
    var highlightCard = function(cardElement) {
    	var previouslySelected = $('.cardSelected');
    	previouslySelected.removeClass("cardSelected");
		cardElement.addClass("cardSelected");
		if (previouslySelected.length > 0) {
			return previouslySelected.get(0);
		} else {
			return null;	
		}
    }
    
    var isTalonBottom = function(column, cardNumber, board) {
    	return cardNumber == board.talon.columns[column].length - 1;
    }
    
	var displayTalon = function(data) {
		$('#talonDisplay').html('');
		for (var column = 0; column < 3; ++column) {
			for (var cardNumber = 0; cardNumber < data.talon.columns[column].length; ++cardNumber) {
				var cardJSON = data.talon.columns[column][cardNumber];
				var card = getCard(cardJSON.name);
				var top = 1 + 3*cardNumber;
				var left = 5 + 10*column;
				var zindex = 1;
				var cardElement = $("<div class='talonCard' style='top:"+top+"em;left:"+left+"em;z-index:"+zindex+"'>"
					+ card.getHTML()
					+ "</div>");
				$('#talonDisplay').append(cardElement);
				cardElement.data("cardJSON", cardJSON);
				if (isTalonBottom(column, cardNumber, data)) {
    				cardElement.click(cardClickHandler);
    			}
				
			}
		}
	}
	
	var displayFreeCell = function(board) {
		$('#freeCellDisplay').html('');
		if (board.freeCell != null) {
			var cardJSON = board.freeCell;
    		var card = getCard(cardJSON.name);
        	var el = $('#freeCellDisplay');
        	var cardElement = $(card.getHTML());
        	el.append(cardElement);
        	cardElement.data("cardJSON", cardJSON);
        	cardElement.click(cardClickHandler);
		}
	}
	
	var displayDeck = function(data) {
		$('#deckDisplay').html('');
    	for (var i = 0; i < data.deck.cards.length/3; ++i) {
    		var card = getCard(data.deck.cards[i].name);
        	var el = $('#deckDisplay');
            //<div style="top: 35px;left: 225px; background-color: blue; height: 50px; width:48px; z-index: 3; position: relative; margin-top: -15px;"> </div>
        	var cardElement = $('<div style="position: absolute; left: ' + 2*i + 'px; top: ' + 2*i + 'px">'+card.getHTML()+'</div>');
    		cardElement.find('div.front').hide();
    		if (i == data.deck.cards.length/3 - 1) {
    			cardElement.click(deckMove);
    		}
        	el.append(cardElement);
    	}
	}
    
    var refreshGameBoard = function(game) {
    	board = game.board;
    	displayPyramid(board);
		displayTalon(board);
		displayFreeCell(board);
		displayDeck(board);
	}
    
    var deckMove = function () {
    	$.post(getGameUrl() + "/moves/deckMove", null, refreshGameBoard, "json");   
    }
    
    $('#loadGame').click(function(){ 
    	$.post(getGameUrl() + "/moves/resetMove", null, refreshGameBoard, "json");   
   	});
    
    $('#randomGame').click(function(){ 
    	$.post("/games/randomGame", null, 
    	function(data) {
    		refreshGameBoard(data);
    		$('#gameName').val(data.id)
    	}, "json");
   	});
    
    $('#engineMove').click(function(){ 
        $.post(getGameUrl() + "/moves/engineMove", null, refreshGameBoard, "json");   
    });
    
    $('#undoMove').click(function(){ 
        $.post(getGameUrl() + "/moves/undo", null, refreshGameBoard, "json");   
    });
    
    $('#undoAllMoves').click(function(){ 
        $.post(getGameUrl() + "/moves/undoAll", null, refreshGameBoard, "json");   
    });

    $('#freeCellDisplay').click(function() {
    	var previouslySelected = $('.cardSelected');
		if (previouslySelected.length > 0) {
			 var cardJSON = jQuery.data(previouslySelected.get(0), "cardJSON");
			   $.ajax({
				      type: "POST",
				      contentType: "application/json",
				      url: getGameUrl() + "/moves/freeCell",
				      data: JSON.stringify(cardJSON),
				      dataType: "json",
				      success: refreshGameBoard
				});
		}
    });
(function() {
	var urlList = {
		'contextPath' : '/14_board/board/',	
		'list' : 'list.do',
		'detail' : 'detail.do',
		'delete' : 'delete.do',
		'update' : 'update.do',
		'insert' : 'insert.do',
		'recommend':'recommend.do',
		'comment' : 'comment.do',
		'delcomment' : 'delcomment.do'
			
	};
	
	var boardModular = {
		init : function() {
			this.template = $('#listtemplate').html();
			this.detail = $('#detailtemplate').html();
			this.doc = $(document);
			this.body = $("body");
			this.boardlist = $(".boardlist");
			this.boarddetail = $(".boarddetail");
			this.fileEle = $("#wFile")[0];
			this.wTitle = $("#wTitle");
			this.dNO = $("#dNO");
			this.dTitle = $("#dTitle");
			this.dContent = $("#dContent");
			this.dWriter = $("#dWriter");
			this.wContent = $("#wContent");
			
			this.doWatiMe();
			this.bindEvent();
			$('.collapsible').collapsible();
			events.on("REFRESH", this.doList);
			events.on("CLEAR_DTEMPLATE", this.doClearDetail);
			events.on("CLEAR_WRITEFROM", this.doClearWriteForm);
			events.on("RECOMMEND_STATUS", this.doRecommandStatus);
			
			events.emit("REFRESH", {limit:100, start:0});
		}, 
		
		doWatiMe : function() {
			var bd = this.body;
			this.doc.ajaxStart(function () {
				bd.waitMe({
					effect: 'ios',
					text: '처리중입니다.',
					bg: 'rgba(255,255,255,0.7)',
					color: '#000',
					source: 'waitme/img.svg'
				});
			})
		    .ajaxStop(function () {
		    	bd.waitMe("hide");	
		    });
		},
		
		render : function(willRender) {
			if(willRender) {
				console.dir(boardModular.template);
				
				Mustache.parse(boardModular.template);
				var rendered = Mustache.render(boardModular.template, willRender);
				boardModular.boardlist.html(rendered);
			}
		},
		
		renderDetail : function(willRender) {
			if(willRender) {
				Mustache.parse(boardModular.detail);
				var rendered = Mustache.render(boardModular.detail, willRender);
				boardModular.boarddetail.html(rendered);
			}
		},
		
		doList : function(reful) {
			var url = urlList.contextPath + reful.limit+"/" + reful.start+"/" + urlList.list;
			var rendering = boardModular.render;
			$.get(url).
			done(function(data) {
				var displayTm =  {
					showData : JSON.parse(data)	
				};
				rendering(displayTm);
			});
			 
			return false;
		},
		
		bindEvent : function() {
			$(document.body).on('click', 'a[id^=detail]', this.doDetail);
			$(document.body).on('click', '#boardUpdate', this.doModify);
			$(document.body).on('click', '#boardDelete', this.doDelete);
			$(document.body).on('click', '#boardRecoomend', this.doRecommend);
			$(document.body).on('click', '#boardComment', this.doComment);
			$(document.body).on('click', '#commentDelete', this.doDeleteComment);
			$(document.body).on('click', '#boardInsert', this.doInsert);
			$(document.body).on('click', '#writeModal', this.doWriteModal);
		},
		
		doDeleteComment : function(event) {
			var aEl = event.target;
			console.log(aEl);
		},
		
		doComment : function(event) {
			
		},
		
		doDetail : function(event) {
			var rendering = boardModular.renderDetail;
			var aEl = event.target;
			$.get(aEl.href).
			done(function(data) {
				rendering(JSON.parse(data));
			});
			return false;
		},
		
		doModify : function() {
			var rendering = boardModular.render;
			var title = boardModular.dTitle == $("#dTitle") ? boardModular.dTitle.val() : $("#dTitle").val();
			var no = boardModular.dNO == $("#dNO") ? boardModular.dNO.val() : $("#dNO").val();
			var content = boardModular.dContent == $("#dContent") ? boardModular.dContent.val() : $("#dContent").val();
			
			$.post(urlList.contextPath + urlList.update,
				{
					title : title,
					no : no,
					content : content
				}
			).done(function(data) {
				if(data == 0) boardModular.doResult('경고', '수정오류 입니다. 다시한번 시도해주세요');
				else {
					boardModular.doResult('완료', '수정 완료 입니다. 감사합니다.');
					events.emit("REFRESH", {limit:100, start:0});
				}
			}).fail(function(data) {
				boardModular.doResult('경고', '수정오류 입니다. 다시한번 시도해주세요');
			});
			return false;
		},
		
		doDelete : function() {
			var rendering = boardModular.render;
			var no = boardModular.dNO == $("#dNO") ? boardModular.dNO.val() : $("#dNO").val();
			var writer = boardModular.dWriter == $("#dWriter") ? boardModular.dWriter.val() : $("#dWriter").val();
			
			$.get(urlList.contextPath + no +"/" + writer + "/" + urlList.delete).
			done(function(data) {
				if(data == 0) boardModular.doResult('경고', '삭제오류 입니다. 다시한번 시도해주세요');
				else {
					if(data === '1000')
						boardModular.doResult('경고', '글쓴 본인이 아닌 경우 삭제할수 없습니다. 감사합니다.');
					else {
						boardModular.doResult('완료', '삭제 완료 입니다. 감사합니다.');
						events.emit("REFRESH", {limit:100, start:0});
						events.emit("CLEAR_DTEMPLATE");
					}
				}
			}).fail(function(data) {
				boardModular.doResult('경고', '삭제오류 입니다. 다시한번 시도해주세요');
			});
			return false;
		},
		
		doInsert : function() {
			var rendering = boardModular.render;
			var fd = new FormData();
			
			var title = boardModular.dWriter == $("#wTitle") ? boardModular.dWriter.val() : $("#wTitle").val();
			var content = boardModular.dWriter == $("#wContent") ? boardModular.dWriter.val() : $("#wContent").val();
			var fileEle = boardModular.fileEle == $("#wFile")[0] ? boardModular.fileEle : $("#wFile")[0];
			
			fd.append("title", title);
			fd.append("content", content);
			for (var i = 0; i < fileEle.files.length; i++) {
				fd.append("attachFile" + i, fileEle.files[i]);
			}
			
			$.ajax({
				url: urlList.contextPath + urlList.insert,
				data: fd,
				type: "POST",
				processData: false,
				contentType: false,
			}).done(function(data) {
				if(data == 0) {
					boardModular.doResult('경고', '글쓰기 오류 입니다. 다시한번 시도해주세요');
				} else {
					events.emit("REFRESH", {limit:100, start:0});
					events.emit("CLEAR_WRITEFROM");
					$('#boardWrite').modal('close');
					boardModular.doResult('완료', '글쓰기 완료 입니다. 감사합니다.');
				}
			}).fail(function() {
				boardModular.doResult('경고', '글쓰기 오류 입니다. 다시한번 시도해주세요');
			});
			return false;
		},
		
		doRecommend : function() {
			var rendering = boardModular.render;
			var no = boardModular.dNO == $("#dNO") ? boardModular.dNO.val() : $("#dNO").val();
			
			$.get(urlList.contextPath + no+"/" + urlList.recommend).
			done(function(data) {
				var result = JSON.parse(data);
				var procecss ;
				console.log(typeof procecss);
				if(result.I) {
					if(result.I == '1') {boardModular.doResult('추천', '추천 처리 완료입니다.'); procecss = 1;}
					else boardModular.doResult('추천', '추천 처리 실패입니다.');
				} 
				
				if(result.D) {
					if(result.D == '1') {boardModular.doResult('추천 취소', '추천 취소 처리 완료입니다.'); procecss = -1;}
					else boardModular.doResult('추천 취소', '추천 취소 처리 실패 입니다.');
				}
				
				events.emit("REFRESH", {limit:100, start:0});
				events.emit("RECOMMEND_STATUS", typeof procecss == 'undefined' ? 'undefined' : procecss );
			});
			return false;
		},
		
		doResult : function(title, content) {
			swal({
			  title: title,
			  html: $('<div>')
			    .addClass('headShake')
			    .text(content),
			  animation: true,
			  customClass: 'animated tada'
			});
		},
		
		doWriteModal : function () {
			$('#boardWrite').modal('open');
		},
		
		doClearDetail : function () {
			boardModular.boarddetail.html("");
		},
		
		doClearWriteForm : function () {
			$("#wTitle").val('');
			$("#wContent").val('');
			$("#wFile").val('');
		}, 
		
		doRecommandStatus : function (process) {
			if(process != 'undefined') {
				$('#dRecommend').val((parseInt($('#dRecommend').val()) + process));
			}
		}
	};
	
	boardModular.init();
	return {
		render : boardModular.render,
		doList : boardModular.doList
	}
})();
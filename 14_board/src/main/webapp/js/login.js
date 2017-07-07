(function() {
	var loginModular = {
		init : function() {
			this.cacheDom();
			this.bindEvent();
			this.doWatiMe();
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
		cacheDom : function() {
			this.doc = $(document);
			this.sh = $('.wrapper');
			this.body = $("body");
			this.id = $('#id');
			this.pass = $('#pass');
			this.result = $('.result');
			this.loginBtn = $('button:eq(0)');
		},
		
		bindEvent : function() {
			this.loginBtn.on('click', this.doLogin.bind(this));		
		},
		
		doLogin : function() {
			var idVal = this.id.val();
			var passVal = this.pass.val();
			var resultVal = this.result;
			var shVal = this.sh;
			$.ajax({
				url:'/14_board/member/loginProcess.do',
				data: {'id':idVal, 'pass':passVal},
				beforeSend: function () {
					if (!idVal || !passVal) {
						resultVal.html('id와  pass를 입력하세요');
							return false;
						}
					}
				}).done(function(data) {
					var result = JSON.parse(data);
					if(result.id) { 
						shVal.hide();
						resultVal.attr('style', 'background-color:blue;color:white;');
						resultVal.html(result.name + "님 반갑습니다. <br> 곧 게시판으로 이동합니다.");
						setInterval(function(){
							window.location.href = '/14_board/board/board.do';
						}, 3000);
					} else {
						resultVal.html("로그인실패");
					}
				});
				return false;
			}
		};
		loginModular.init();
})();
(function() {
	var urlList = {
		'contextPath' : '/14_board/board/',	
		'list' : 'list.do',
		'detail' : 'detail.do',
		'delete' : 'delete.do',
		'update' : 'update.do',
		'insert' : 'insert.do'		
	};
	
	this.template = $('#template').html();
	this.doc = $(document);
	this.body = $("body");
	this.wrapper = $(".wrapper");
	
	var boardModular = {
		init : function() {
			this.doWatiMe();
			this.render();
			this.bindEvent();
		}, 
		
		doWatiMe : function() {
			var bd = body;
			doc.ajaxStart(function () {
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
		
		render : function(mtmpl, willRender) {
			if(willRender && !mtmpl) {
				Mustache.parse(template);
				var rendered = Mustache.render(template, willRender);
				wrapper.html(rendered);
			} else {
				if(mtmpl) {
					Mustache.parse(mtmpl);
					var rendered = Mustache.render(mtmpl, willRender);
					wrapper.html(rendered);
				}
			}
		},
		
		doList : function(limit, start) {
			var rendering = this.render;
			$.get(urlList.contextPath + "/"+limit+"/" + "/"+start+"/" + urlList.list).
			done(function(data) {
				var tm = `
						<table>
					    <thead>
					      <tr>
					        <th>글번호</th>
					        <th>제목</th>
					        <th>작성자</th>
					        <th>작성일</th>
					      </tr>
					    </thead>
						<tbody>
						{{#showData}}
							<tr>
					        <td>{{no}}</td>
					        <td><a id='detail{{no}}' href=`+urlList.contextPath+`{{no}}/`+urlList.detail+`>{{title}}</a></td>
					        <td>{{writer}}</td>
					        <td>{{reg_date}}</td>
					      	</tr>
					    {{/showData}}
						</tbody>
						</table>
				`;
				var displayTm =  {
					showData : JSON.parse(data)	
				};
				rendering(tm, displayTm);
			});
			return false;
		},
		
		bindEvent : function() {
			$(document.body).on('click', 'a[id^=detail]', this.doDetail);
		},
		
		doDetail : function(event) {
			var rendering = boardModular.render;
			var aEl = event.target;
			$.get(aEl.href).
			done(function(data) {
				console.log(data);
				var tm = `
					<table>
					<tbody>
					<tr><td>글번호</td><td>{{no}}</td></tr>
					<tr><td>제목</td><td>{{title}}</td></tr>
					<tr><td>글쓴이</td><td>{{writer}}</td></tr>
					<tr><td>글쓴날</td><td>{{reg_date}}</td></tr>
					<tr><td>글내용</td><td>{{content}}</td></tr>
					</tbody>
					</table>
					<div><a href="http://localhost:9000/14_board/board/board.do">홈</a></div>
				`;
				rendering(tm, JSON.parse(data));
			});
			return false;
		}
	};
	
	boardModular.init();
	boardModular.doList(10, 1);
	return {
		render : boardModular.render,
		doList : boardModular.doList
	}
})();
$(function () {
$(document).ready(function() {
   console.log("abc");
   
   Highcharts.setOptions({
            global: {
                useUTC: false
            }
         });
      $('#container').highcharts({ 
    	  
            chart: {
                  type: 'spline',
		  animation: Highcharts.svg,
                  marginRight: 10,
                  events: {
                      load: function() {
                          var series = this.series;
                          setInterval(function() {
                           var url = "/matricserver.do?method=post";  // ʵʱ��ȡ��������
           $.ajax({
                url: url,
                type: "post",
                cache: false,
                dataType: "json",
                data: {},
                ifModified: false,
                success: function(result){
              jQuery.each(result, function(m, obj) {
               var x = (new Date()).getTime();
               var y0 = obj.value0;
               var y1 = obj.value1;
               series[0].addPoint([x, parseInt(y0)], true, true);
               series[1].addPoint([x, parseInt(y1)], true, true);//����������Ϊtrue������ʵʱ�ػ�ͼ��
                       });
               }
            });
                          }, 30000);
                      }
                  }
              },

		        title: {
		          text: 'CPU/�ڴ�ʹ���ʶ�̬����ͼ'
		        },
		        xAxis: {
		          title: {
		            text: 'ʱ��'
		          },
		          type: 'datetime',
		          tickPixelInterval: 150
		        },
		        yAxis: {
		          title: {
		            text: 'ʹ����'
		          },
		          plotLines: [{
		              value: 0,
		              width: 10,
		              color: '#808080'
		            }]
		        },
		        //������ĳ������ʱ����ʾ��Ϣ
		        //dateFormat,numberFormat��highCharts�Ĺ�����
		        tooltip: {
		          formatter: function() {
		            return '<b>' + this.series.name + '</b><br/>' +
		                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
		                    Highcharts.numberFormat(this.y, 2);
		          }
		        },
		        //���ߵ�ʾ��˵�������ͼ�ϵ�ͼ��˵��һ��
		        legend: {
            			enabled: false
              		 },
		       plotOptions: {
                  series: {
                      cursor: 'pointer',
                      point: {
                          events: {
                              click: function() {
                                  hs.htmlExpand(null, {
                                      pageOrigin: {
                                          x: this.pageX,
                                          y: this.pageY
                                      },
                                      headingText: this.series.name,
                                      maincontentText: 'ֵ��' + this.y +':<br/>ʱ�䣺'+
                                          Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', this.x),
                                      width: 200
                                  });
                              }
                          }
                      },
                      marker: {
                          lineWidth: 1
                      }
                  }
              },
              series: [{
                  name: 'cpu������'
              }, {
                  name: '�ڴ�������'
              }]

	   });
   });
});

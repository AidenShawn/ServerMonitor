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
                           var url = "/matricserver.do?method=post";  // 实时获取最新数据
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
               series[1].addPoint([x, parseInt(y1)], true, true);//第三个参数为true，才能实时重画图表
                       });
               }
            });
                          }, 30000);
                      }
                  }
              },

		        title: {
		          text: 'CPU/内存使用率动态曲线图'
		        },
		        xAxis: {
		          title: {
		            text: '时间'
		          },
		          type: 'datetime',
		          tickPixelInterval: 150
		        },
		        yAxis: {
		          title: {
		            text: '使用率'
		          },
		          plotLines: [{
		              value: 0,
		              width: 10,
		              color: '#808080'
		            }]
		        },
		        //鼠标放在某个点上时的提示信息
		        //dateFormat,numberFormat是highCharts的工具类
		        tooltip: {
		          formatter: function() {
		            return '<b>' + this.series.name + '</b><br/>' +
		                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
		                    Highcharts.numberFormat(this.y, 2);
		          }
		        },
		        //曲线的示例说明，像地图上得图标说明一样
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
                                      maincontentText: '值：' + this.y +':<br/>时间：'+
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
                  name: 'cpu利用率'
              }, {
                  name: '内存利用率'
              }]

	   });
   });
});

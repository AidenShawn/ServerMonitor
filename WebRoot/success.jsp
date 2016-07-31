<%@ page language="java" contentType="text/html;charset=utf-8" import="com.struts.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTDHTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<link rel="stylesheet" type="text/css"
	href="http://www.highcharts.com/highslide/highslide.css" />
<script type="text/javascript">
$(function () {
    $(document).ready(function() {
        Highcharts.setOptions({
            global: {
                useUTC: false 
            }
        }); 
        
        var flag = true;
        $('#pause').click(function() {
          if(flag){
            flag = false;
            this.value = "Start";
          }else{
            flag = true;
            this.value = "Pause";
          }
        });
        
        var chart;
        var time = (new Date()).getTime();
        $('#container').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg,
                marginRight: 10, 
                events: {
                    load: function() {
                        var series = this.series;
                        setInterval(function() {
                        if(flag){
                          var url = "<%=basePath%>/matricserver.do";  // 实时获取最新数据
                              $.ajax({
                                  url: url,
                                  type: "post",
                                  cache: false,
                                  dataType: "json",
                                  data: {},
                                  ifModified: false,
                                  success: function(data, textStatus, jqXHR){
                                    var index = data.length - 10;
		                            if(index < 0) index = 0;
		                            var array = data.slice(index, data.length);
                                    jQuery.each(array, function(m, obj) {
                                      index = index + 1;
                                      var x = time + index * 5000;
                                      var y0 = obj.cpuratio;
                                      var y1 = obj.memratio;
                                      series[0].addPoint([x, parseInt(y0)], true, true);
                                      series[1].addPoint([x, parseInt(y1)], true, true);//第三个参数为true，才能实时重画图表
                                    });
                                  },
                              });
                            }
                         }, 5000);
                    }
                }
            },
            title: {
                text: 'Live CPU & Memory Ratio'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 20
            },                                                                      
            yAxis: {
                title: {
                    text: 'Value'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                crosshairs: true,
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>百分比'+
                        Highcharts.numberFormat(this.y, 2) +'<br/>时间：'+
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x); 
                }
            }, 
            credits: {
            	enabled: false
            },   
            legend: {
                align: 'left',
                verticalAlign: 'top',
                y: 20,
                floating: true,
                borderWidth: 0                                                     
            },                                                                      
            series: [{
                  name: 'cpu利用率',
                  data: (function() {                                                 
                    var data = [],                                                                                
                        i;                                                          
                                                                                    
                    for (i = -19; i <= 0; i++) {                                    
                        data.push({                                                 
                            x: time + i * 5000,                                     
                            y: 0                                        
                        });                                                         
                    }                                                               
                    return data;                                                    
                })()                                                                
              }, {
                  name: '内存利用率',
                  data: (function() {                                                 
                    var data = [],                                                                                
                        i;                                                          
                                                                                    
                    for (i = -19; i <= 0; i++) {                                    
                        data.push({                                                 
                            x: time + i * 5000,                                     
                            y: 0                                        
                        });                                                         
                    }                                                               
                    return data;                                                    
                })()
              }]
        });
      
    });
});
</script>
<title></title>
</head>
<body>
	<div style="width:1000px;height:700px;overflow:auto;">
		<div id="container"
			style="min-width: 100px; height: 400px; margin: 0 auto"></div>
	    <div><input type="button" id="pause" value="Pause" > </div>
	</div>
</body>
</html>


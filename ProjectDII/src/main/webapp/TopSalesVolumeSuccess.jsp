<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang=""> <!--<![endif]-->
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Data analyzing">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ECharts</title>
    <!-- 引入刚刚下载的 ECharts 文件 -->
    <script src="echarts.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@2.2.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.4/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-match-height@0.7.2/dist/jquery.matchHeight.min.js"></script>
    <script src="assets/js/main.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/normalize.css@8.0.0/normalize.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/pixeden-stroke-7-icon@1.2.3/pe-icon-7-stroke/dist/pe-icon-7-stroke.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.2.0/css/flag-icon.min.css">
    <link rel="stylesheet" href="assets/css/cs-skin-elastic.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <!-- <script type="text/javascript" src="https://cdn.jsdelivr.net/html5shiv/3.7.3/html5shiv.min.js"></script> -->
    <link href="https://cdn.jsdelivr.net/npm/chartist@0.11.0/dist/chartist.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/jqvmap@1.5.1/dist/jqvmap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/images.css">
    <link href="https://cdn.jsdelivr.net/npm/weathericons@2.1.0/css/weather-icons.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/fullcalendar@3.9.0/dist/fullcalendar.min.css" rel="stylesheet" />
</head>
<body>
<aside id="left-panel" class="left-panel">
    <nav class="navbar navbar-expand-sm navbar-default">
        <div id="main-menu" class="main-menu collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active">
                    <a href="index.jsp"><i class="menu-icon fa fa-laptop"></i>Dashboard </a>
                </li>
                <li class="menu-title">SEARCH</li><!-- /.menu-title -->
                <li>
                    <a href="search.jsp"><i class="menu-icon fa fa-search"></i>Search</a>
                </li>
                <li class="menu-title">CHARTS</li><!-- /.menu-title -->
                <li class="menu-item-has-children dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-bar-chart"></i>Average</a>
                    <ul class="sub-menu children dropdown-menu">
                        <li><i class="menu-icon fa fa-line-chart"></i><a href="AverageSalesVolume.jsp">Album Average</a></li>
                        <li><i class="menu-icon fa fa-area-chart"></i><a href="AverageGenreTracksVolume.jsp">Style Average</a></li>
                    </ul>
                </li>
                <li class="menu-item-has-children dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-table"></i>TOPN</a>
                    <ul class="sub-menu children dropdown-menu">
                        <li><i class="menu-icon fa fa-line-chart"></i><a href="TopSalesVolume.jsp">Sales Volume TOP100</a></li>
                        <li><i class="menu-icon fa fa-area-chart"></i><a href="TopScore.jsp">Score TOP100</a></li>
                    </ul>
                </li>
                <li class="menu-item-has-children dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-paper-plane"></i>WordCount</a>
                    <ul class="sub-menu children dropdown-menu">
                        <li><i class="menu-icon fa fa-line-chart"></i><a href="WordCountRoles.jsp">Roles</a></li>
                        <li><i class="menu-icon fa fa-area-chart"></i><a href="WordCountCountries.jsp">Countries</a></li>
                    </ul>
                </li>
                </li>
                <li class="menu-title">Extras</li><!-- /.menu-title -->
                <li class="menu-item-has-children dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <i class="menu-icon fa fa-glass"></i>Pages</a>
                    <ul class="sub-menu children dropdown-menu">
                        <li><i class="menu-icon fa fa-sign-in"></i><a href="page-login.html">Login</a></li>
                        <li><i class="menu-icon fa fa-file-o"></i><a href="page-register.html">Register</a></li>
                        <li><i class="menu-icon fa fa-paper-plane"></i><a href="page-forget.html">Forget Pass</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </nav>
</aside><!-- /#left-panel -->
<div id="right-panel" class="right-panel">
    <!-- Header-->
    <header id="header" class="header">
        <div class="top-left">
            <div class="navbar-header">
                <!--                <a class="navbar-brand" href="./"><img src="" alt="Logo"></a>-->
                <!--                <a class="navbar-brand hidden" href="./"><img src="" alt="Logo"></a>-->
                <a id="menuToggle" class="menutoggle"><i class="fa fa-bars"></i></a>
            </div>
        </div>
        <div class="top-right">
            <div class="header-menu">
                <div class="user-area dropdown float-right">
                    <a href="#" class="dropdown-toggle active" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <img class="user-avatar rounded-circle" src="images/admin.jpg" alt="">
                    </a>
                    <div class="user-menu dropdown-menu">
                        <a class="nav-link" href="page-login.html"><i class="fa fa-power -off"></i>Logout</a>
                        <a class="nav-link" href="page-register.html"><i class="fa fa-power -off"></i>Register</a>
                        <a class="nav-link" href="page-forget.html"><i class="fa fa-power -off"></i>Change password</a>
                    </div>
                </div>

            </div>

        </div>
    </header>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
    <div id="main" style="width: 1200px;height:680px;"></div>
</div>
<script type="text/javascript">

   //将后台的数据取出
   var topn1 = JSON.parse(JSON.stringify(${TopN1}));
   var list = topn1.rows;
   var dataset = [];
   var album = [];
   var num = [];
   for (var item of list) {
       dataset.push({name:item.rowkey,value:item.value})
       album.push(item.rowkey)
       album.push(item.value)
   }
    /*var album = [];
    var num = [];
    for (var item of list) {
        album.push(item.rowkey)
        num.push(item.value)
    }*/

    // 基于准备好的dom，初始化echarts实例
   var myChart = echarts.init(document.getElementById('main'));


    // 指定图表的配置项和数据

   var option = {
       
       tooltip: {
           trigger: 'item',
           formatter: '{a} <br/>{b} : {c}%'
       },
       toolbox: {
           feature: {
               dataView: { readOnly: false },
               restore: {},
               saveAsImage: {}
           }
       },
       legend: {
           data: album
       },
       series: [
           {
               name: 'Funnel',
               type: 'funnel',
               left: '10%',
               top: 60,
               bottom: 60,
               width: '80%',
               min: 0,
               max: 100,
               minSize: '0%',
               maxSize: '100%',
               sort: 'ascending',
               gap: 2,
               label: {
                   show: true,
                   position: 'inside'
               },
               labelLine: {
                   length: 10,
                   lineStyle: {
                       width: 1,
                       type: 'solid'
                   }
               },
               itemStyle: {
                   borderColor: '#fff',
                   borderWidth: 1
               },
               emphasis: {
                   label: {
                       fontSize: 20
                   }
               },
               data: dataset
           }
       ]
   };
    /*var option = {
        title: {
            text: '最好销量'
        },
        tooltip: {},
        legend: {
            data: ['年份']
        },
        xAxis: {
            data: album
        },
        yAxis: {},
        series: [
            {
                name: '销量',
                type: 'bar',
                data: num
            }
        ]
    };*/

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>
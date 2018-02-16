var filesCurrentPage=1;
var usersCurrentPage=1;

window.onload=function(){
  $(document).ready(function() {


    //Files list
    var filesTotalRows = $('#tblData').find('tbody tr:has(td)').length;
    var filesRecordPerPage = 5;
    var filesTotalPage = Math.ceil(filesTotalRows / filesRecordPerPage);
    var $pages = $('<div id="pages" class="pagination"></div>');
    $('<span class="pageNumber"><</span>').appendTo($pages);
    for (i = 0; i < filesTotalPage; i++) {
      $('<span class="pageNumber">' + (i + 1) + '</span>').appendTo($pages);
    }
    $('<span class="pageNumber">></span>').appendTo($pages);
    $('<p id="pagenumber"></p>').appendTo($pages);
    $pages.appendTo('#tblData');

    //Users list
    var usersTotalRows = $('#tblDataU').find('tbody tr:has(td)').length;
    var usersRecordPerPage = 2;
    var usersTotalPage = Math.ceil(usersTotalRows / usersRecordPerPage);
    var $pagesU = $('<div id="pagesU" class="pagination"></div>');
    $('<span class="pageNumber"><</span>').appendTo($pagesU);
    for (i = 0; i < usersTotalPage; i++) {
      $('<span class="pageNumber">' + (i + 1) + '</span>').appendTo($pagesU);
    }
    $('<span class="pageNumber">></span>').appendTo($pagesU);
    $('<p id="pagenumberU"></p>').appendTo($pagesU);
    $pagesU.appendTo('#tblDataU');

    //Common
    $('.pageNumber').hover(
      function() {
        $(this).addClass('focus');
      },
      function() {
        $(this).removeClass('focus');
      }
    );

    //initial records of file list
    $('#tblData').find('tbody tr:has(td)').hide();
    var tr = $('#tblData tbody tr:has(td)');
    var begin=-1;
    var end=-1;
    if(filesTotalRows>filesRecordPerPage){
      begin=1;
      end=filesRecordPerPage;
    }
    else{
      if(filesTotalRows>0){
        begin=1;
        end=filesTotalRows;
      }
    }
    for (var i = 0; i <= filesRecordPerPage - 1; i++) {
      $(tr[i]).show();
    }
    document.getElementById("pagenumber").innerHTML = "Showing " + begin + "-" + end + " of " + filesTotalRows;

    //initial records of user list
    $('#tblDataU').find('tbody tr:has(td)').hide();
    var trU = $('#tblDataU tbody tr:has(td)');
    var beginU=-1;
    var endU=-1;
    if(usersTotalRows>usersRecordPerPage){
      beginU=1;
      endU=usersRecordPerPage;
    }
    else{
      if(usersTotalRows>0){
        beginU=1;
        endU=usersTotalRows;
      }
    }
    for (var i = 0; i <= usersRecordPerPage - 1; i++) {
      $(trU[i]).show();
    }
    document.getElementById("pagenumberU").innerHTML = "Showing " + beginU + "-" + endU + " of " + usersTotalRows;


    $('#pages span').click(function(event) 
    {
      var nBegin;
      var nEnd;
      if($(this).text()=="<")
      {
        if(filesCurrentPage!=1)
        {  
          filesCurrentPage--;
          $('#tblData').find('tbody tr:has(td)').hide();
          nBegin = (filesCurrentPage - 1) * filesRecordPerPage;
          nEnd = filesCurrentPage * filesRecordPerPage - 1;
          
          for (var i = nBegin; i <= nEnd; i++) {
            $(tr[i]).show();
          }
          if((nEnd+1) > filesTotalRows)
          {
            nEnd=filesTotalRows-1;
          }
          document.getElementById("pagenumber").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + filesTotalRows;
        }
      }
      else if($(this).text()==">")
      {
        if(filesCurrentPage!=filesTotalPage)
        {
          filesCurrentPage++;
          $('#tblData').find('tbody tr:has(td)').hide();
          nBegin = (filesCurrentPage - 1) * filesRecordPerPage;
          nEnd = filesCurrentPage * filesRecordPerPage - 1;
          
          for (var i = nBegin; i <= nEnd; i++) {
            $(tr[i]).show();
          }
          if((nEnd+1) > filesTotalRows)
          {
            nEnd=filesTotalRows-1;
          }
          document.getElementById("pagenumber").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + filesTotalRows;
        }
      }
      else
      {  
        $('#tblData').find('tbody tr:has(td)').hide();
        nBegin = ($(this).text() - 1) * filesRecordPerPage;
        nEnd = $(this).text() * filesRecordPerPage - 1;
        filesCurrentPage=$(this).text();
        
        for (var i = nBegin; i <= nEnd; i++) {
          $(tr[i]).show();
        }
        if((nEnd+1) > filesTotalRows)
        {
          nEnd=filesTotalRows-1;
        }
        document.getElementById("pagenumber").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + filesTotalRows;
      } 
    });

    $('#pagesU span').click(function(event) 
    {
      var nBegin;
      var nEnd;
      if($(this).text()=="<")
      {
        if(usersCurrentPage!=1)
        {  
          usersCurrentPage--;
          $('#tblDataU').find('tbody tr:has(td)').hide();
          nBegin = (usersCurrentPage - 1) * usersRecordPerPage;
          nEnd = usersCurrentPage * usersRecordPerPage - 1;
          
          for (var i = nBegin; i <= nEnd; i++) {
            $(trU[i]).show();
          }
          if((nEnd+1) > usersTotalRows)
          {
            nEnd=usersTotalRows-1;
          }
          document.getElementById("pagenumberU").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + usersTotalRows;
        }
      }
      else if($(this).text()==">")
      {
        if(usersCurrentPage!=usersTotalPage)
        {
          usersCurrentPage++;
          $('#tblDataU').find('tbody tr:has(td)').hide();
          nBegin = (usersCurrentPage - 1) * usersRecordPerPage;
          nEnd = usersCurrentPage * usersRecordPerPage - 1;
          
          for (var i = nBegin; i <= nEnd; i++) {
            $(trU[i]).show();
          }
          if((nEnd+1) > usersTotalRows)
          {
            nEnd=usersTotalRows-1;
          }
          document.getElementById("pagenumberU").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + usersTotalRows;
        }
      }
      else
      {  
        $('#tblDataU').find('tbody tr:has(td)').hide();
        nBegin = ($(this).text() - 1) * usersRecordPerPage;
        nEnd = $(this).text() * usersRecordPerPage - 1;
        usersCurrentPage=$(this).text();
        
        for (var i = nBegin; i <= nEnd; i++) {
          $(trU[i]).show();
        }
        if((nEnd+1) > usersTotalRows)
        {
          nEnd=usersTotalRows-1;
        }
        document.getElementById("pagenumber").innerHTML = "Showing " +(nBegin+1) + "-" + (nEnd+1) + " of " + usersTotalRows;
      } 
    });



  });
}
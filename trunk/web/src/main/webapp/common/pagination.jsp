<input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}"/>
<input type="hidden" name="page.orderBy" id="orderBy" value="${page.orderBy}"/>
<input type="hidden" name="page.order" id="order" value="${page.order}" />
<div id="footer">
    current ${page.pageNo} page, total ${page.totalPages} page
    <a href="javascript:jumpPage(1)">first</a>
    <s:if test="page.hasPre"><a href="javascript:jumpPage(${page.prePage})">previou</a></s:if>
    <s:if test="page.hasNext"><a href="javascript:jumpPage(${page.nextPage})">next</a></s:if>
    <a href="javascript:jumpPage(${page.totalPages})">last</a>
</div>
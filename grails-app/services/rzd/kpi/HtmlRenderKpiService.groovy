package rzd.kpi

import rzd.kpi.aux.WorkersAndIndexes;
import rzd.kpi.aux.WorkersAndIndexes.KpiData
import grails.transaction.Transactional

@Transactional
class HtmlRenderKpiService {
	WorkersAndIndexes resultData = WorkersAndIndexes.getInstance()


	def cssString = """
      <style type="text/css">
        H1,H2,H3,H4,H5,H6
        {
         text-align:left;
         color:maroon;
         font-family:Arial,Geneva,Helvetica,sans-serif;
        }
table {
    font-family: "Trebuchet MS", sans-serif;
    font-size: 16px;
    font-weight: bold;
    line-height: 1.4em;
    font-style: normal;
    border-collapse:separate;
}
th{
    padding:15px;
    color:#fff;
    text-shadow:1px 1px 1px #568F23;
    border:1px solid #93CE37;
    border-bottom:3px solid #9ED929;
    background-color:#9DD929;
    background:-webkit-gradient(
        linear,
        left bottom,
        left top,
        color-stop(0.02, rgb(123,192,67)),
        color-stop(0.51, rgb(139,198,66)),
        color-stop(0.87, rgb(158,217,41))
        );
    background: -moz-linear-gradient(
        center bottom,
        rgb(123,192,67) 2%,
        rgb(139,198,66) 51%,
        rgb(158,217,41) 87%
        );
    -webkit-border-top-left-radius:5px;
    -webkit-border-top-right-radius:5px;
    -moz-border-radius:5px 5px 0px 0px;
    border-top-left-radius:5px;
    border-top-right-radius:5px;
}
td {
    padding:10px;
    text-align:center;
    background-color:#DEF3CA;
    border: 2px solid #E7EFE0;
    -moz-border-radius:2px;
    -webkit-border-radius:2px;
    border-radius:2px;
    color:#666;
    text-shadow:1px 1px 1px #fff;
}
     </style>
   """

/**
 * Возвращает строку с html, содержащим таблицу показателей для 
 * указанного имени сотрудника.
 * @param name
 * @return
 */
	def getHtmlByUser(String name) {
		List<KpiData> data = resultData.getUsers().get(name)
		String beginDate = resultData.beginDate
		String endDate = resultData.endDate
		def html =  """
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Показатели ${name}</title>
${cssString}
</head>
<body>
<h2>Показатели сотрудника:<br> ${name}</h1>
<h5>за период от ${beginDate} до ${endDate}</h5>
<table>
<tr>
<th>Показатель</th><th>Целевое значение</th><th>Фактическое значение</th>
</tr>
"""
		for(KpiData kpi : data) {
			html += "<tr><td>${kpi.index.metric_name}</td><td>${kpi.index.target_value}</td><td>${kpi.value}</td></tr>"
		}
		html += "</table></body></html>"
		return html
	}

	/**
	 * Возвращает список имен сотрудников.
	 * @return Set строк
	 */
	def getWorkersList() {
		return resultData.getUsers().keySet()
	}

	/**
	 * Установить параметры стилей в виде строки формата 
	 * "<style type="text/css"> .... </style>"
	 * @param cssString - строка с описанием стилей
	 * @return
	 */
	def setCssString(String cssString) {
		this.cssString = cssString
	}
	


}

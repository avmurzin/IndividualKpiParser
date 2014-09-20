package rzd.kpi.aux

class WorkersAndIndexes {
	private static final INSTANCE = new WorkersAndIndexes()

	class KpiData {
		String value
		WorkerIndexes index
	}

	Map<String, Object> users = new HashMap<String, Object>()
	String beginDate, endDate

	private WorkersAndIndexes() {}

	static getInstance(){
		return INSTANCE
	}

	def cssString = """
      <style type="text/css">
        H1,H2,H3,H4,H5,H6
        {
         text-align:center;
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
	 * Очистить структуру.
	 * @return
	 */
	def clearAll(String beginDate, String endDate) {
		this.beginDate = beginDate
		this.endDate = endDate
		users.clear()
	}
	/**
	 * Добавить данные о показателе сотрудника; если сотрудника в списке еще нет,
	 * то он добавляется с пустым списком показателей.
	 * @param user - имя сотрудника
	 * @param kpi - значение показателя
	 * @param index - описание показателя @see WorkerIndexes
	 * @return
	 */
	def add(String user, String kpi, WorkerIndexes index) {
		if (!users.containsKey(user)) {
			List<KpiData> kpis = new ArrayList<KpiData>()
			users.put(user, kpis)
		}
		KpiData kpiData = new KpiData(value: kpi, index: index)
		users.get(user).add(kpiData)

	}
	/**
	 * Получить список имен сотрудников.
	 * @return Set<String> имен пользователей
	 */
	Set<String> getWorkersList() {
		return users.keySet()
	}

	String getWorkerKpiHtml(String name) {
		List<KpiData> data = users.get(name)
		def html =  """
           <!DOCTYPE html>
           <html>
            <head>
              <meta charset="utf-8">
              <title>Показатели ${name}</title>
              ${cssString}
              </head>
              <body>
               <h1>Показатели сотрудника: ${name}</h1>
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

//	Map<String, Object> getData() {
//		for (key in users.keySet()) {
//			for (view in users.get(key)) {
//				println "${key}: ${view.value}: ${view.index.metric_name}"
//			}
//
//		}
//	}
}

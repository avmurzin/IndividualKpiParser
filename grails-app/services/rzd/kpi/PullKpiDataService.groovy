package rzd.kpi

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.web.json.JSONObject
import rzd.kpi.aux.Branches
import rzd.kpi.aux.WorkerIndexes
import rzd.kpi.aux.WorkersAndIndexes

@Transactional
class PullKpiDataService {
	
	List<WorkerIndexes> indexes = new ArrayList<WorkerIndexes>()
	List<Branches> branches = new ArrayList<Branches>()
	WorkersAndIndexes resultData = WorkersAndIndexes.getInstance()
	
	def hostName = 'http://gvc-api-v3.orw.oao.rzd'
	def indexesListUrl = '/nsi/metric/?object=сотрудники'
	def branchListUrl = '/nsi/branch'
	//"code" == "function_name" at WorkerIndexes
	def pullIndexesUrl = "/kpi?beginDate={beginDate}&branch={branch}&code={code}&endDate={endDate}&level=сотрудники"
		
	RestBuilder rest = new RestBuilder()

	/**
	 * Подготовить массив значений показателей всех видов для каждого сотрудника 
	 * каждого подразделения за неделю от текущей даты.
	 * Данные сохраняются в @see WorkerAndIndexes.
	 * @return nothing
	 */
	def calculateKpi() {
		calculateKpi(7)
	}
	
	/**
	 * Подготовить массив значений показателей всех видов для каждого сотрудника 
	 * каждого подразделения за указанное число дней назад от текущей даты.
	 * Данные сохраняются в @see WorkerAndIndexes.
	 * @return nothing
	 */
    def calculateKpi(int daysAgo) {
		Calendar calendar = new GregorianCalendar()
		SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy")
		String dateToday = formattedDate.format(calendar.getTime())
		calendar.add(Calendar.DAY_OF_MONTH, (-1)*daysAgo)
		String dateWeekAgo = formattedDate.format(calendar.getTime())

		calculateKpi(dateWeekAgo, dateToday)
    }
	
	/**
	 * Подготовить массив значений показателей всех видов для каждого сотрудника каждого подразделения.
	 * @param beginDate - начальная дата в формате дд.мм.гггг 
	 * @param endDate - конечная дата в формате дд.мм.гггг 
	 * Данные сохраняются в @see WorkerAndIndexes.
	 * @return nothing
	 */
	def calculateKpi(String beginDate, String endDate) {
		
		getIndexesList()
		getBranchesList()
		resultData.clearAll(beginDate, endDate)

		for (Branches branch : branches) {
			for (WorkerIndexes index : indexes) {
				def urlVariables = [beginDate: "${beginDate}", endDate: "${endDate}", code: "${index.function_name}", branch:"${branch.zo}"]
				def httpResp = rest.get("${hostName}${pullIndexesUrl}", urlVariables)
				httpResp.json instanceof JSONObject
				httpResp.json.each {
					resultData.add("${it.object}", "${it.kpi}", index)
				}
			}
		}
	}

	/**
	 * Изменить URL хоста для закачки данных.
	 * @param hostName - имя хоста (без финального /)
	 * @return
	 */
	def setHostName(String hostName) {
		if (!hostName.equals("")) {
			this.hostName = hostName
		}
	}
		
	def getIndexesList() {
		indexes.clear()
		def httpResp = rest.get("${hostName}${indexesListUrl}")
		httpResp.json instanceof JSONObject
		httpResp.json.each {
			WorkerIndexes index = new WorkerIndexes();
			index.function_name = "${it.function_name}"
			index.metric_name = "${it.metric_name}"
			index.target_value = it.target_value
			indexes.add(index)
			index = null
		}
	}
	
	def getBranchesList() {
		branches.clear()
		def httpResp = rest.get("${hostName}${branchListUrl}")
		httpResp.json instanceof JSONObject
		httpResp.json.each {
			Branches branch = new Branches();
			branch.zo = "${it.zo}"
			branch.zo_name = "${it.zo_name}"
			branches.add(branch)
			branch = null
		}
	}
	
	
}


package rzd.kpi

import grails.plugins.rest.client.RestBuilder
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import rzd.kpi.aux.Branches
import rzd.kpi.aux.WorkerIndexes
import rzd.kpi.aux.WorkersAndIndexes

@Transactional
class PullKpiDataService {
	
	List<WorkerIndexes> indexes = new LinkedList<WorkerIndexes>()
	List<Branches> branches = new LinkedList<Branches>()
	WorkersAndIndexes resultData = WorkersAndIndexes.getInstance()
	
	def hostName = 'http://gvc-api-v3.orw.oao.rzd'
	def indexesListUrl = '/nsi/metric/?object=сотрудники'
	def branchListUrl = '/nsi/branch'
	//"code" == "function_name" at WorkerIndexes
	def pullIndexesUrl = "/kpi?beginDate={beginDate}&branch={branch}&code={code}&endDate={endDate}&level=сотрудники"
		
	RestBuilder rest = new RestBuilder()

    def calculateKpi() {
		//TODO: реализация вызова с beginDate - текущее время, endDate - минус неделя
		calculateKpi('08.09.2014', '15.09.2014')
    }
	
	def calculateKpi(String beginDate, String endDate) {
		
		getIndexesList()
		getBranchesList()
		
		for (Branches branch : branches) {
			for (WorkerIndexes index : indexes) {
				def urlVariables = [beginDate: "${beginDate}", endDate: "${endDate}", code: "${index.function_name}", branch:"${branch.zo}"]
				def httpResp = rest.get("${hostName}${pullIndexesUrl}", urlVariables)
				httpResp.json instanceof JSONObject
				httpResp.json.each {
					resultData.add("${it.object}", "${it.kpi}")
					
				}
			}
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
			branches.add(branch)
			branch = null
		}
	}
}


//	RestBuilder rest = new RestBuilder()
//	def urlVariables = [key:grailsApplication.config.meetup.key, topic : "london", time : ",1w"]
//	def url = "${grailsApplication.config.meetup.baseurl}/2/open_events?key={key}&topic={topic}&time={time}"
//	def resp = rest.get(url, urlVariables)
	
//	def hostName = 'http://192.168.1.2:8080/AvroraStorageUI-0.1'
//	def getTree = '/tree'
//	def newFolder = '/new_folder/{tree_id}?name={name}&description={description}'

// 08.09.2014

//
//		def urlVariables = [tree_id: "c2b238b1-24e3-4f4b-bc55-2765abd19be7", name:"Grails_folder", description : "Grails rulezz"]
//		def resp1 = rest.get("${hostName}${newFolder}", urlVariables)
//
//		resp1.json instanceof JSONObject
//
//		resp1.json.each {
//				//println "id:${it.id}, value:${it.value}"
//			println "${it}"
//			}
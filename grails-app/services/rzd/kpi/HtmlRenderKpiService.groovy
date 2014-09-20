package rzd.kpi

import rzd.kpi.aux.WorkersAndIndexes;
import grails.transaction.Transactional

@Transactional
class HtmlRenderKpiService {
	WorkersAndIndexes resultData = WorkersAndIndexes.getInstance()
	
    def getHtmlByUser(String name) {
		return resultData.getWorkerKpiHtml(name)
    }
	
	def getWorkersList() {
		return resultData.getWorkersList()
	}
}

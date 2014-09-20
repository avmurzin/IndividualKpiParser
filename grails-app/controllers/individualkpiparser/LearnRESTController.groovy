package individualkpiparser

import grails.converters.JSON

class LearnRESTController {
def pullKpiDataService
def HtmlRenderKpiService
    def index() {
		pullKpiDataService.calculateKpi()
		render HtmlRenderKpiService.getWorkersList() as JSON
	}
	
	def kpi() {
		String name = params.name
		render HtmlRenderKpiService.getHtmlByUser(name)
	}
}

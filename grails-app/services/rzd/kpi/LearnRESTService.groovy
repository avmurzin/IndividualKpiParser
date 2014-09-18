package rzd.kpi

import grails.transaction.Transactional
import groovyx.net.http.HTTPBuilder

@Transactional
class LearnRESTService {



	def serviceMethod() {
		def http = new HTTPBuilder( 'http://192.168.1.2:8080' )

		def resp = http.get( path: '/AvroraStorageUI-0.1/all_users') { resp, json ->

			println resp.status
			json.each {
				println "id:${it.id}, name:${it.value}"
			}




		}
	}
	
}

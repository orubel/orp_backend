package net.nosegrind

import org.grails.core.DefaultGrailsControllerClass
//import main.scripts.net.nosegrind.apiframework.Method
import grails.util.Metadata

class ApidocController {

	def apiCacheService
	
	def index(){
		redirect(action:'show')
	}
	
	def show(){
		println("apidoc > show called")
		Map docs = [:]
		
		grailsApplication.controllerClasses.each { DefaultGrailsControllerClass controllerClass ->
			String controllername = controllerClass.logicalPropertyName

			def cache = apiCacheService.getApiCache(controllername)
			if(cache){
				cache[params.apiObject].each() { it ->

					if (!['deprecated', 'defaultAction', 'currentStable'].contains(it.key)) {
						if(!docs["${controllername}"]){
							docs["${controllername}"] =[:]
						}
						String action = it.key

						docs["${controllername}"]["${action}"] = cache[params.apiObject][action]['doc']
					}
				}
			}
		}

		return ['apidoc':docs]
	}

}


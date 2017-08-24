package net.nosegrind


//import grails.transaction.Transactional
//import main.scripts.net.nosegrind.apiframework.TraceService

//@Transactional
class EventTypeController {

	static defaultAction = 'list'
	//TraceService traceService

	Long today = System.currentTimeMillis()

	def getEvent(){
		def event =  EventType.get(params.id.toLong())
		return [eventType:event]
	}

	def list(){
		def result = EventType.list()
		return [eventType:result]
	}
}

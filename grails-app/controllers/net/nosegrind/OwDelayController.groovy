package net.nosegrind



//import grails.transaction.Transactional
//import main.scripts.net.nosegrind.apiframework.TraceService

//@Transactional
class OwDelayController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def listByPacketReport(){
		def result = OwDelay.executeQuery("select new map(OD.id as id, OD.timestamp as timestamp) from OwDelay as OD where OD.report.id=? order by OD.timestamp desc",[params.id.toLong()])
		return [owDelay:result]
	}

	def getById(){
		def result = OwDelay.executeQuery("select new map(OD.vals as values) from OwDelay as OD where OD.id=?",[params.id?.toLong()])
		def val = Eval.me(result.values[0])
		LinkedHashMap vals = ["values":val]
		return [owDelay:vals]
	}
}

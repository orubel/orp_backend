package net.nosegrind



//import grails.transaction.Transactional
//import main.scripts.net.nosegrind.apiframework.TraceService

//@Transactional
class PacketTraceController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def getArchiveLocationsByEvent(){
		def result = PacketTrace.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketTrace as PT join PT.report as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id=? group by PT.report.id ",[params.id.toLong()])
		//def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ?",[event.id.toLong()])
		return [packetTrace:result]
	}

	def listByPacketReport(){
		def result = PacketLossRate.executeQuery("select new map(PT.rtt as rtt, PT.ttl as ttl) from PacketTrace as PT join PT.ip as IP where PT.report.id=? order by PT.ttl asc",[params.id.toLong()])
		return [packetTrace:result]
	}

}

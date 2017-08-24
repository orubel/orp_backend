package net.nosegrind



//import grails.transaction.Transactional
//@Transactional
class PacketRetransmitController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def getArchiveLocationsByEvent(){
		//EventType event = EventType.findByName(params.id)
		def result = PacketRetransmit.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketRetransmit as PRT join PRT.report as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ? group by PRT.report.id ",[params.id.toLong()])
		//def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ?",[event.id.toLong()])
		return [packetTrace:result]
	}

	def listByPacketReport(){
		def result = PacketRetransmit.executeQuery("select new map(PR.timestamp as timestamp, PR.val as val) from PacketRetransmit as PR where PR.report.id=? order by PR.timestamp asc",[params.id.toLong()])
		return [packetTrace:result]
	}

}

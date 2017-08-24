package net.nosegrind



//import grails.transaction.Transactional
//@Transactional
class PacketRetransmitSubController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def getArchiveLocationsByEvent(){
		//EventType event = EventType.findByName(params.id)
		def result = PacketRetransmit.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketRetransmit as PRT join PRT.report as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ? and PRT.report.id in (select report from PacketRetransmit) group by PRT.report.id ",[params.id.toLong()])
		//def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ?",[event.id.toLong()])
		return [packetRetransmitSub:result]
	}

	def listByPacketReport(){
		def result = PacketRetransmit.executeQuery("select new map(SI.start as start, SI.duration as duration) from PacketRetransmitSub as PRS join PRS.packetRetransmit as PR join PRS.sub as SI join PR.report as R where R.id=? order by SI.start asc",[params.id.toLong()])

		def cnt = result.size()

		// INIT MAP
		LinkedHashMap newMap = [:]
		result.each(){
			def data = 9000/(it.duration).toFloat()
			newMap[it.start] = data
		}

		LinkedHashMap results = [values:newMap]
		return [packetRetransmitSub:results]
	}

}

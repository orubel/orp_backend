package net.nosegrind



//import grails.transaction.Transactional
//@Transactional
class ThroughputSubController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def getPacketReportId(){
		def result
		if(params.id){
			 result = PacketReport.executeQuery("select R.id from PacketReport as R where R.id=?",[params.id])
		}else{
			result = PacketReport.executeQuery("select R.id from PacketReport as R",[max: 1])
		}
		return [throughputSub:result]
	}

	def getArchiveLocationsByEvent(){
		//EventType event = EventType.findByName(params.id)
		def result = PacketRetransmit.executeQuery("select new map(R.id as id,LIS.url as inputSource, LID.url as inputDestination) from Throughput as T join T.report as R join R.arch as A join A.inputSource as LIS join A.inputDestination as LID where R.eventType.id = ? and T.report.id in (select report from Throughput) group by T.report.id ",[params.id.toLong()])
		//def result = PacketReport.executeQuery("select new map(PR.id as id,LIS.url as inputSource, LID.url as inputDestination) from PacketReport as PR join PR.arch as A join A.inputSource as LIS join A.inputDestination as LID where PR.eventType.id = ?",[event.id.toLong()])
		return [throughputSub:result]
	}

	def listByPacketReport(){
		def result = PacketRetransmit.executeQuery("select new map(SI.start as start, SI.duration as duration) from ThroughputSub as TS join TS.throughput as T join TS.sub as SI join T.report as R where R.id=? order by SI.start asc",[params.id.toLong()])

		def cnt = result.size()

		// INIT MAP
		LinkedHashMap newMap = [:]
		result.each(){
			def data = 9000/(it.duration).toFloat()
			newMap[it.start] = data
		}

		LinkedHashMap results = [values:newMap]
		return [throughputSub:results]
	}

}

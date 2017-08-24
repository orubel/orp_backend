package net.nosegrind



//import grails.transaction.Transactional
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.text.DecimalFormat

//@Transactional
class PacketLossRateController {
                             
	static defaultAction = 'listByEvent'
	//TraceService traceService

	def listByPacketReport(){
		Long endDate = System.currentTimeMillis() / 1000L
		Long diff = ((60*60)*24)*28
		Long startDate = (endDate-diff).toLong()

		def result = PacketLossRate.executeQuery("select new map(PRD.xAxis as xAxis,PRD.yAxis as yAxis) from PacketLossRate as PRD where PRD.report.id=? and PRD.xAxis<=${endDate} and PRD.xAxis>=${startDate} order by PRD.xAxis asc",[params.id.toLong()])
		return [packetLossRate:result]
	}

	def listByPacketReportforHeatMap(){
		DecimalFormat df = new DecimalFormat("#.######")

		Long endTime = System.currentTimeMillis() / 1000L
		Long diff = ((60*60)*24)*28
		Long startTime = (endTime-diff).toLong()

		def result = PacketLossRate.executeQuery("select new map(PRD.xAxis as xAxis,PRD.yAxis as yAxis) from PacketLossRate as PRD where PRD.report.id=? and PRD.xAxis<=${endTime} and PRD.xAxis>=${startTime} order by PRD.xAxis asc",[params.id.toLong()])

		//Double total = null
		//Double mean = null
		//Integer cnt = result.size()
		Double highest = null
		Double lowest = null

		// INIT MAP
		LinkedHashMap newMap = [:]


		result.each() { it ->
			List temp = getDateString(it.xAxis.toLong())

			if (newMap[temp[0]] == null) {
				newMap[temp[0]] = [dow:temp[1], woy:temp[2], color:'#FF0000', values:[df.format(it.yAxis.toDouble())], highest:df.format(it.yAxis.toDouble())]
			}else{
				newMap[temp[0]].values.each(){ it2->
					if(it2.toDouble()>newMap[temp[0]].highest.toDouble()){
						newMap[temp[0]].highest = df.format(it2)
					}
				}
				String tempAxis = df.format(it.yAxis.toDouble())
				newMap[temp[0]].values.add(tempAxis.toDouble())
			}

			String tempAxis = df.format(it.yAxis.toDouble())
			if(highest==null || tempAxis.toDouble()>highest){ highest = tempAxis.toDouble() }
			if(lowest==null || tempAxis.toDouble()<lowest){ lowest = tempAxis.toDouble() }
			//total = (total==null)?it.yAxis.toDouble():total+it.yAxis.toDouble()
		}
		//mean = total/cnt

		// CALC BOUNDS
		Double yDiff = df.format(highest-lowest).toDouble()
		Double boundsDiff = df.format(yDiff/5).toDouble()
		LinkedHashMap heatmapBounds = [:]
		Double currentBoundsDiff = lowest
		int i = 0
		List colors = ['#FF0000', '#FF9100', '#F2FF00', '#9DFF00', '#00FF00']
		while(currentBoundsDiff<highest){
			Double low = currentBoundsDiff
			Double high = (low + boundsDiff)-0.000001
			heatmapBounds[i] = [low:df.format(low).toDouble(),high:df.format(high).toDouble(),color:colors[i]]
			currentBoundsDiff += boundsDiff
			i++
		}

		newMap.each() { key, val ->
			heatmapBounds.each() { key2, val2 ->

				if (val.highest.toDouble() >= val2.low && val.highest.toDouble() <= val2.high) {
					newMap[key].color = val2.color
				}
			}
		}
		LinkedHashMap finalMap = formatCalendar(newMap)

		LinkedHashMap results = [values:finalMap]
		return [packetLossRate:results]
	}


	private LinkedHashMap formatCalendar(LinkedHashMap results){
		LinkedHashMap newResults = results.sort()
		// newMap[temp[0]] = [dow:temp[1], woy:temp[2], color:'#FF0000', values:[df.format(it.yAxis.toDouble())], highest:df.format(it.yAxis.toDouble())]

		int currentWeek = 0
		int currentDay = 0
		List weeks = []
		results.each(){ key, val ->
			// NEW WEEK: Check for missing days in LAST WEEK, Test against 1 for this week
			if(val.woy>currentWeek) {
				// Fill in days for last week
				for(int i=val.dow;i<7;i++){
					List nmap = createFakeDate(currentWeek, i)
					newResults[nmap[0]] = [dow:nmap[1], woy:nmap[2], color:'#ffffff', values:[], highest:0]
				}
				// Fill in days for this week
				for(int i=1;i<val.dow;i++){
					List nmap = createFakeDate(val.woy, i)
					newResults[nmap[0]] = [dow:nmap[1], woy:nmap[2], color:'#ffffff', values:[], highest:0]
				}
				currentWeek = val.woy
				currentDay = val.dow
				if(!weeks.contains(currentWeek)){ weeks.add(currentWeek) }
			}else if ((val.dow - currentDay) > 1) {
				// Fill in days for this week
				for(int i=currentDay;i<val.dow;i++){
					List nmap = createFakeDate(val.woy, i)
					newResults[nmap[0]] = [dow:nmap[1], woy:nmap[2], color:'#ffffff', values:[], highest:0]
				}
				currentDay = val.dow
			}else{
				List nmap = createFakeDate(val.woy, val.dow)
				newResults[nmap[0]] = [dow:nmap[1], woy:nmap[2], color:val.color, values:[], highest:val.highest]
				currentDay = val.dow
			}
		}
		weeks.sort()
		newResults['weekCnt'] = weeks
		return newResults
	}

	private List createFakeDate(Integer woy, Integer dow){
		Calendar cal = Calendar.getInstance()
		cal.set(Calendar.WEEK_OF_YEAR, woy)
		switch(dow){
			case 1:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
				break
			case 2:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
				break
			case 3:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
				break
			case 4:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
				break
			case 5:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
				break
			case 6:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
				break
			case 7:
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
				break
		}

		int yr = cal.get(Calendar.YEAR)
		int mn = cal.get(Calendar.MONTH)
		int dy = cal.get(Calendar.DAY_OF_MONTH)

		String date = "${yr}"+(mn<10?("0${mn}"):("${mn}"))+(dy<10?("0${dy}"):("${dy}"))+"0000"
		DateFormat dfm = new SimpleDateFormat("yyyyMMddHHmm")
		Long unixtime = dfm.parse(date).getTime()
		unixtime=unixtime/1000

		return [unixtime,dow, woy]
	}

	private List getDateString(Long unixTime){
		Date startDate = new Date ()
		startDate.setTime(unixTime*1000)
		Calendar cal = Calendar.getInstance()
		cal.setTime(startDate)
		int yr = cal.get(Calendar.YEAR)
		int mn = cal.get(Calendar.MONTH)
		int dy = cal.get(Calendar.DAY_OF_MONTH)
		int dow = cal.get (Calendar.DAY_OF_WEEK)
		int woy = cal.get(Calendar.WEEK_OF_YEAR)

		String date = "${yr}"+(mn<10?("0${mn}"):("${mn}"))+(dy<10?("0${dy}"):("${dy}"))+"0000"
		DateFormat dfm = new SimpleDateFormat("yyyyMMddHHmm")
		Long unixtime = dfm.parse(date).getTime()
		unixtime=unixtime/1000

		return [unixtime,dow,woy]
	}

}

package rzd.kpi.aux

class WorkersAndIndexes {
	private static final INSTANCE = new WorkersAndIndexes()
	
	private WorkersAndIndexes() {}
	
	static getInstance(){
		return INSTANCE
	}
		
	class KpiData {
		String value
		WorkerIndexes index
	}

	Map<String, Object> users = new HashMap<String, Object>()
	String beginDate, endDate

	/**
	 * Удалить собранные данные.
	 * @return
	 */
	def clearAll(String beginDate, String endDate) {
		this.beginDate = beginDate
		this.endDate = endDate
		users.clear()
	}
	
	/**
	 * Добавить данные в список показателей сотрудника; если сотрудника 
	 * в списке еще нет, то он добавляется с пустым списком показателей.
	 * @param user - имя сотрудника
	 * @param kpi - значение показателя
	 * @param index - описание показателя @see WorkerIndexes
	 * @return
	 */
	def add(String user, String kpi, WorkerIndexes index) {
		if (!users.containsKey(user)) {
			List<KpiData> kpis = new ArrayList<KpiData>()
			users.put(user, kpis)
		}
		KpiData kpiData = new KpiData(value: kpi, index: index)
		users.get(user).add(kpiData)
	}
}

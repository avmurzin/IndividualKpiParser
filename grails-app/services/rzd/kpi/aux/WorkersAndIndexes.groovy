package rzd.kpi.aux

class WorkersAndIndexes {
	List<WorkerIndexes> indexes = new LinkedList<WorkerIndexes>()
	//Map<String, WorkersAndIndexes> workers = new HashMap<String, WorkersAndIndexes>()

	private static final INSTANCE = new WorkersAndIndexes()
	
	private WorkersAndIndexes() {}
	
	static getInstance(){
		return INSTANCE
	}
	
	static add(String worker, String kpi) {
		//TODO: 
	}
}

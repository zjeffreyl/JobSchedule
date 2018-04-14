import java.util.ArrayList;

public class JobSchedule {

	private ArrayList<Job> jobs;
	
	public JobSchedule() {
		jobs = new ArrayList<Job>();
	}

	public Job addJob(int time) {
		Job job = new Job();
		job.time = time;
		jobs.add(job);
		return job;
	}
	
	public Job getJob(int index) {
		return jobs.get(index);
	}
	
	public int minCompletionTime() {
		ArrayList<Job> lastJobs = new ArrayList<Job>();
		for(int i = 0; i < jobs.size(); i++) {
			if(jobs.get(i).cycle) {
				return -1;
			}
			if(jobs.get(i).outgoingJobs.isEmpty()) {
				lastJobs.add(jobs.get(i));
			}
		}
		int longestJob = 0;
		//requires time to complete the last job
		for(int i = 0; i < lastJobs.size(); i++) {
			if(longestJob < lastJobs.get(i).startTime + lastJobs.get(i).time) {
				longestJob = lastJobs.get(i).startTime + lastJobs.get(i).time;
			}
		}
		return longestJob;
	}
	
	class Job{
		
		private ArrayList<Job> incomingJobs = new ArrayList<Job>();
		private ArrayList<Job> outgoingJobs = new ArrayList<Job>();
		private ArrayList<Job> visited = new ArrayList<Job>();
		private int time;
		private boolean cycle = false;
		//initialize start to zero with no prerequisite
		private int startTime = 0;
		
		public void requires(Job j) {
			if(j.startTime + j.time > startTime || incomingJobs.isEmpty()) {
				//replace start time if it is bigger than current
				startTime = j.time + j.startTime;
			}
			incomingJobs.add(j);
			j.outgoingJobs.add(this);
			visited.add(this);
			updateStartTimes(this);
			visited.removeAll(visited);
		}
		
		public void updateStartTimes(Job current) {
			//no need to update current
			if(current.outgoingJobs.isEmpty()) {
				return;
			}else {
				//update next outgoing
				for(int i = 0; i < current.outgoingJobs.size(); i++) {
					//cycle detection
					if(visitedContains(current.outgoingJobs.get(i))) {
						//cycle detected
						startTime = -1;
						current.startTime= -1;
						cycle = true;
						return;
					}
					//identified the next node but need to check for THE valid start time
					visited.add(current.outgoingJobs.get(i));
					current.outgoingJobs.get(i).startTime = current.time + current.startTime;
					updateStartTimes(current.outgoingJobs.get(i));
				}
			}
		}
		
		public boolean visitedContains(Job j) {
			for(int i = 0 ; i < visited.size(); i++) {
				if(visited.get(i).equals(j)) {
					return true;
				}
			}
			return false;
		}
		
		public int getStartTime() {
			return startTime;
		}
	}
}

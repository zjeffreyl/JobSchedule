import static org.junit.Assert.*;

import org.junit.Test;

public class JobScheduleTest {

	@Test
	public void test() {
		//basic split and cycle
		int expected = 0;
		JobSchedule jobSchedule1 = new JobSchedule();
		jobSchedule1.addJob(8);//0
		jobSchedule1.addJob(3);//1
		jobSchedule1.addJob(5);//2
		jobSchedule1.addJob(12);//3
		jobSchedule1.addJob(6);//4
		jobSchedule1.getJob(2).requires(jobSchedule1.getJob(3));
		jobSchedule1.getJob(3).requires(jobSchedule1.getJob(4));
		jobSchedule1.getJob(1).requires(jobSchedule1.getJob(2));
		jobSchedule1.getJob(0).requires(jobSchedule1.getJob(2));
		expected = 23;
		assertEquals(expected, jobSchedule1.getJob(0).getStartTime());
		assertEquals(expected, jobSchedule1.getJob(1).getStartTime());
		expected = 31;
		assertEquals(expected, jobSchedule1.minCompletionTime());
		JobSchedule.Job merger = jobSchedule1.addJob(13);//5
		merger.requires(jobSchedule1.getJob(0));
		merger.requires(jobSchedule1.getJob(1));
		expected = 31;
		assertEquals(expected, jobSchedule1.getJob(5).getStartTime());
		expected = 44;
		assertEquals(expected, jobSchedule1.minCompletionTime());
		
		
		//Given Expected Test
		JobSchedule schedule = new JobSchedule();
		schedule.addJob(8); //adds job 0 with time 8
		JobSchedule.Job j1 = schedule.addJob(3); //adds job 1 with time 3
		schedule.addJob(5); //adds job 2 with time 5
		expected = 8;
		assertEquals(expected, schedule.minCompletionTime()); //should return 8, since job 0 takes time 8 to complete
		schedule.getJob(0).requires(schedule.getJob(2)); //job 2 must precede job 0
		expected = 13;
		assertEquals(expected, schedule.minCompletionTime()); //should return 13 (job 0 cannot start until time 5)
		schedule.getJob(0).requires(j1); //job 1 must precede job 0
		expected = 13;
		assertEquals(expected, schedule.minCompletionTime()); //should return 13
		expected = 5;
		assertEquals(expected, schedule.getJob(0).getStartTime()); //should return 5
		expected = 0;
		assertEquals(expected, j1.getStartTime()); //should return 0
		expected = 0;
		assertEquals(expected, schedule.getJob(2).getStartTime()); //should return 0
		j1.requires(schedule.getJob(2)); //job 2 must precede job 1
		expected = 16;
		assertEquals(expected, schedule.minCompletionTime()); //should return 16
		expected = 8;
		assertEquals(expected, schedule.getJob(0).getStartTime()); //should return 8
		expected = 5;
		assertEquals(expected, schedule.getJob(1).getStartTime()); //should return 5
		expected = 0;
		assertEquals(expected, schedule.getJob(2).getStartTime()); //should return 0
		schedule.getJob(1).requires(schedule.getJob(0)); //job 0 must precede job 1 (creates loop)
		expected = -1;
		assertEquals(expected, schedule.minCompletionTime()); //should return -1
		assertEquals(expected, schedule.getJob(0).getStartTime()); //should return -1
		assertEquals(expected, schedule.getJob(1).getStartTime()); //should return -1
		expected = 0;
		assertEquals(expected, schedule.getJob(2).getStartTime()); //should return 0 (no loops in prerequisites)
		
		//cycle test
		JobSchedule jobSchedule2 = new JobSchedule();
		jobSchedule2.addJob(4);//0
		jobSchedule2.addJob(7);//1
		jobSchedule2.addJob(20);//2
		jobSchedule2.addJob(14);//3
		jobSchedule2.addJob(15);//4
		jobSchedule2.addJob(8);//5
		jobSchedule2.getJob(4).requires(jobSchedule2.getJob(5));
		jobSchedule2.getJob(4).requires(jobSchedule2.getJob(3));
		expected = 14;
		assertEquals(expected, jobSchedule2.getJob(4).getStartTime());
		expected = 29;
		assertEquals(expected, jobSchedule2.minCompletionTime());
		jobSchedule2.getJob(1).requires(jobSchedule2.getJob(2));
		jobSchedule2.getJob(0).requires(jobSchedule2.getJob(1));
		expected = 31;
		assertEquals(expected, jobSchedule2.minCompletionTime());
		jobSchedule2.getJob(2).requires(jobSchedule2.getJob(4));
		jobSchedule2.getJob(3).requires(jobSchedule2.getJob(1));
		expected = -1;
		assertEquals(expected, jobSchedule2.minCompletionTime());
		
		//branches
		JobSchedule jobSchedule3 = new JobSchedule();
		jobSchedule3.addJob(4);//0
		jobSchedule3.addJob(7);//1
		jobSchedule3.addJob(20);//2
		jobSchedule3.addJob(14);//3
		jobSchedule3.addJob(15);//4
		jobSchedule3.addJob(8);//5
		jobSchedule3.getJob(4).requires(jobSchedule3.getJob(5));
		jobSchedule3.getJob(3).requires(jobSchedule3.getJob(4));
		jobSchedule3.getJob(2).requires(jobSchedule3.getJob(4));
		expected = 43;
		assertEquals(expected, jobSchedule3.minCompletionTime());
		jobSchedule3.getJob(1).requires(jobSchedule3.getJob(4));
		jobSchedule3.getJob(0).requires(jobSchedule3.getJob(4));
		assertEquals(expected, jobSchedule3.minCompletionTime());
		jobSchedule3.addJob(90);//6
		jobSchedule3.getJob(6).requires(jobSchedule3.getJob(0));
		expected = 117;
		assertEquals(expected, jobSchedule3.minCompletionTime());
		
		JobSchedule jobSchedule4 = new JobSchedule();
		jobSchedule4.addJob(4);//0
		jobSchedule4.addJob(7);//1
		jobSchedule4.addJob(20);//2
		jobSchedule4.addJob(14);//3
		jobSchedule4.addJob(15);//4
		jobSchedule4.addJob(8);//5
		
		jobSchedule4.addJob(28);//6
		jobSchedule4.getJob(0).requires(jobSchedule4.getJob(1));
		jobSchedule4.getJob(1).requires(jobSchedule4.getJob(2));
		jobSchedule4.getJob(2).requires(jobSchedule4.getJob(3));
		jobSchedule4.getJob(3).requires(jobSchedule4.getJob(4));
		jobSchedule4.getJob(4).requires(jobSchedule4.getJob(5));
		jobSchedule4.getJob(5).requires(jobSchedule4.getJob(0));
		jobSchedule4.getJob(6).requires(jobSchedule4.getJob(3));
		expected = 51;
		assertEquals(expected, jobSchedule4.getJob(6).getStartTime());
		expected = -1;
		assertEquals(expected, jobSchedule4.minCompletionTime());
		
		
	}

}

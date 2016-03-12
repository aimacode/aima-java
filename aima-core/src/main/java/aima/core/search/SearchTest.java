SearchTest {

   @Test
   /**
    * Function testing all function of Search.java
    */
	public void testAll() {
		Search.checkStep(1);//Selecting Left Sock
		Search.checkStep(2);//Selecting right Sock
		Search.checkStep(3);//Selecting left Shoe
		Search.checkStep(4);//Selecting Right Shoe
		assertEquals("Expecting Success",0,Search.checkIfGoal());//Checking if goal state is reached
		String[] a1={null,null,null,null};
		
		a1[0]=new String("Left Sock");
		a1[1]= new String("Right Sock");
		a1[2]=new String("Left Shoe");
		a1[3]= new String("Right Shoe");
		for(int i=0;i<4;i++)
		assertEquals("",a1[i],Search.path[i]);//Testing each element of path individually
	}
}

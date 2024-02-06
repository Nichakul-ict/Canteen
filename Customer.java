// File: Customer
// Description: implement the following methods takeAction() and add ADDITIONAL CODE
// Project: 1
//
// ID: 6588178
// Name: Nichakul Kongnual
// Section: 2
//
// On my honor, Nichakul Kongnual, this project assignment is my own work
// and I have not provided this code to any other students.


import java.util.ArrayList;
import java.util.List;

public class Customer {
	
	//*********************** DO NOT MODIFY ****************************//
	public static enum CustomerType{DEFAULT, STUDENT, PROFESSOR, ATHLETE, ICTSTUDENT};	//Different types of customers 
	private static int customerRunningNumber = 1;	//static variable for assigning a unique ID to a customer
	private CanteenICT canteen = null;	//reference to the CanteenICT object
	private int customerID = -1;		//this customer's ID
	protected CustomerType customerType = CustomerType.DEFAULT;	//the type of this customer, initialized with a DEFAULT customer.
	protected List<FoodStall.Menu> requiredDishes = new ArrayList<FoodStall.Menu> ();	//List of required dishes
	
	public static enum Payment{DEFAULT, CASH, MOBILE};
	public static final int[] PAYMENT_TIME = {3, 2, 1};
	protected Payment payment = Payment.DEFAULT;
	
	protected int state = 0; // 0 wait-to-enter, 1 wait-to-order, 2 ordering, 
							 // 3 making payment, 4 wait-to-seat, 5 siting, 
							 // 6 eating, 7 done
	//*****************************************************************//
	
	
	/**
	 * Constructor. Initialize canteen reference, default customer type, and default payment method. 
	 * 				Initialize other values as needed
	 * @param _canteen
	 */
	public Customer(CanteenICT _canteen)
	{
		//******************* YOUR CODE HERE **********************
		this.canteen = _canteen;
		customerID = customerRunningNumber;
		customerRunningNumber++;
		requiredDishes.add(FoodStall.Menu.NOODLES);
		requiredDishes.add(FoodStall.Menu.DESSERT);
		requiredDishes.add(FoodStall.Menu.MEAT);
		requiredDishes.add(FoodStall.Menu.SALAD);
		requiredDishes.add(FoodStall.Menu.BEVERAGE);
		this.state = 1;
		//*****************************************************
	}
	
	/**
	 * Constructor. Initialize canteen reference, default customer type, and specific payment method.
	 * 				Initialize other values as needed 
	 * @param _canteen
	 * @param payment
	 */
			
	public Customer(CanteenICT _canteen, Payment payment)	
	{
		//******************* YOUR CODE HERE **********************
		this.canteen = _canteen;
		this.payment = payment;
		customerID = customerRunningNumber;
		customerRunningNumber++;
		requiredDishes.add(FoodStall.Menu.NOODLES);
		requiredDishes.add(FoodStall.Menu.DESSERT);
		requiredDishes.add(FoodStall.Menu.MEAT);
		requiredDishes.add(FoodStall.Menu.SALAD);
		requiredDishes.add(FoodStall.Menu.BEVERAGE);
		this.state = 1;
		//*****************************************************
	}
	
	
	
	/**
	 * Depends on the current state of the customer, different action will be taken
	 * @return true if the customer has to move to the next queue, otherwise return false
	 */
	public boolean takeAction()
	{
		//************************** YOUR CODE HERE **********************//
		FoodStall list = null;
		if(state == 1) {
			if(this.canteen.getwaitToEnterQueue().get(0) == this) {
				for(int i=0 ; i<this.canteen.getFoodStalls().size(); i++) {
					if(this.canteen.getFoodStalls().get(i).getCustomerQueue().size()!=FoodStall.MAX_QUEUE) {
						if(list == null || this.canteen.getFoodStalls().get(i).getCustomerQueue().size()<list.getCustomerQueue().size()) {
							if(this.canteen.getFoodStalls().get(i).getMenu().containsAll(getRequiredFood())){
								list = this.canteen.getFoodStalls().get(i);
							}
						}	
					}
				}
			}
			if(list != null && flag==0) {
				if(list.getCustomerQueue().size()<5) {
					list.getCustomerQueue().add(this);
					this.canteen.getwaitToEnterQueue().remove(this);
					
					flag=1;
					jot("@" + this.getCode() + "-" + state +" queues up at " + list.getName() + ", and waiting to order.");
					state++;

				}
			}
		}
		
		else if(state == 2) {
		//The customer orders food and waits for food to be ready in the food stall queue.
			for(int i=0 ; i<this.canteen.getFoodStalls().size();i++) {
//				if(this.canteen.getFoodStalls().get(i).getCustomerQueue().size() == 0) {
//					continue;
//				}
				if(this.canteen.getFoodStalls().get(i).getCustomerQueue().size() != 0) {
					if(this.canteen.getFoodStalls().get(i).getCustomerQueue().get(0).getCustomerID() ==this.customerID) {
						if(this.canteen.getFoodStalls().get(i).isWaitingForOrder()) {
							
							this.canteen.getFoodStalls().get(i).takeOrder(getRequiredFood());
							jot("@" + this.getCode() + "-" + state + " orders from " + this.canteen.getFoodStalls().get(i).getName() + ", and will need to wait for "+ 9 + " periods to cook.");
							state++;
							
						}
					}
				}	
			}
		}
		else if(state == 3) {
	     //When the food is ready, the customer makes a payment and waits for the payment transaction to be completed in the food stall queue.
			for(int i=0 ; i<this.canteen.getFoodStalls().size();i++) {
				if(this.canteen.getFoodStalls().get(i).getCustomerQueue().size() != 0) {
					if(this.canteen.getFoodStalls().get(i).getCustomerQueue().get(0).getCustomerID() ==this.customerID) {
						if (this.canteen.getFoodStalls().get(i).isReadyToServe()) {
							this.canteen.getFoodStalls().get(i).takePayment(getPayment());
							jot("@" + this.getCode() + "-" + state + " pays at " + this.canteen.getFoodStalls().get(i).getName() + " using " + PAYMENT_TIME[payment.ordinal()] + " which required.");
							state++;
			
						}
					}
				}
			}
		}
		if(state == 4) {
			//if(this.canteen.getFoodStalls())
			for(int i=0 ; i<this.canteen.getFoodStalls().size();i++) {
				if(this.canteen.getFoodStalls().get(i).getCustomerQueue().size() != 0) {
					if(this.canteen.getFoodStalls().get(i).getCustomerQueue().get(0).getCustomerID() ==this.customerID) {
						if (this.canteen.getFoodStalls().get(i).isPaid()) {
							this.canteen.getFoodStalls().get(i).serve();
							canteen.getWaitToSeatQueue().add(this);
							canteen.cloneEnter.add(this);
							jot("@" + this.getCode() + "-" + state + " retrieves food from " + this.canteen.getFoodStalls().get(i).getName() + ", and goes to Waiting-to-Seat Queue.");
							state++;
							return true;
						}
					}
				}
			}
		}
		if(state == 5) {
			if (!this.canteen.getWaitToSeatQueue().isEmpty()) {
				if(this.canteen.getWaitToSeatQueue().get(0).getCustomerID() ==this.customerID) {	
				for (int i=0; i<canteen.getTable().size(); i++) {
						this.canteen.getWaitToSeatQueue().remove(this);
						this.canteen.getTable().get(i).getSeatedCustomers().add(this);
						
						sitTable=this.canteen.getTable().get(i);
						state++;
						break;
				}
				jot("@" + this.getCode() + "-" + (state-1) + " sits at Table " + sitTable.getID()+".");
				return true;
				}
				
			}
		}
		if(state == 6) {
			//The customer starts eating at the table until finished.
			this.Starteat();
			this.eattime();
			jot("@" + this.getCode() + "-" + state + " eats at the table, and will need "+ getEatTime() +" periods to eat his/her meal.");
			state++;
		}
		
		if(state == 7) {
			if(eattime() >= 28) {
				for(int i=0 ; i<this.canteen.getTable().size();i++) {
					if(this.canteen.getTable().get(i).getSeatedCustomers().contains(this)) {
						this.canteen.getDoneQueue().add(this);
						this.canteen.getTable().get(i).getSeatedCustomers().remove(this);
						jot("@" + this.getCode() + "-" + state + " is done eating.");
						state++;
					}
				
				}
			}
		}
		return false;
		//**************************************************************//
		
	}
	



	//******************************************** YOUR ADDITIONAL CODE HERE (IF ANY) *******************************//
	
	public static int flag=0;
	private int Starteat = 0;
	private int eattime = 0;
	public int Starteat() {
		this.Starteat = canteen.getCurrentTime();
		return this.Starteat;
		
	}
	public int eattime() {
		this.eattime = canteen.getCurrentTime() - this.Starteat;
		return this.eattime;
		
	}
	
	public int getEatTime() {
		int temp=0;
		for(FoodStall.Menu i: requiredDishes) {
			temp+=FoodStall.EAT_TIME[i.ordinal()];
		}
		return temp;
	}
	
	Table sitTable;
	
	
	//****************************************************************************************************//
				
	

	//***************For hashing, equality checking, and general purposes. DO NOT MODIFY **************************//	
	
	public CustomerType getCustomerType()
	{
		return this.customerType;
	}
	
	public int getCustomerID()
	{
		return this.customerID;
	}
	
	public Payment getPayment()
	{
		return this.payment;
	}
	
	public List<FoodStall.Menu> getRequiredFood()
	{
		return this.requiredDishes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + customerID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customerID != other.customerID)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", customerType=" + customerType +", payment="+payment.name()+"]";
	}

	public String getCode()
	{
		return this.customerType.toString().charAt(0)+""+this.customerID;
	}
	
	/**
	 * print something out if VERBOSE is true 
	 * @param str
	 */
	public void jot(String str)
	{
		if(CanteenICT.VERBOSE) System.out.println(str);
		
		if(CanteenICT.WRITELOG) CanteenICT.append(str, canteen.name+"_state.log");
	}


	//*************************************************************************************************//
	
}

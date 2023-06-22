package kashkoala; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;



public class backbone implements Serializable {
	
	static JFrame frame;
	JLabel label;
	static JPanel panel;
	static JPanel home;
	static JPanel portfolio;

	
	static Map<String, ArrayList<String>> friendships = new HashMap<String, ArrayList<String>>();

	///using hashmap with a list instead of multiple maps
	static Map<String, String[]> login_info2 = new HashMap<String, String[]>();
	static String[] info_list = new String[4];	
	
	
	//a hashmap containing user information correlated with their owned shares
	static Map<String, ArrayList<Tuple>> portfolio_info = new HashMap<String, ArrayList<Tuple>>();
	//a string list that contains how many shares are owned, price paid and current price	

	
	
	
	static boolean logged_in = false;
	static String logged_user = "";

	
	public backbone()  {
		//this is the login screen that launches initially 
		frame = new JFrame();
		
		JButton button = new JButton("Login");
		JButton button2 = new JButton("Register");
		JButton button3 = new JButton("Exit");
		
		button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                create_login_popup();
            }
        });
		
		button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
					create_registration_popup();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                }
        });
		
		button3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
				
		});
		
		//we create a panel and add it to the JFrame, then add the buttons to the panel
		//this will allow us to dispose of the current panel and replace it with the home screen & portfolio screens
		
		panel = new JPanel();
		
		panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
		panel.setLayout(new GridLayout(0,1));
		
		panel.add(button);
		panel.add(button2);
		panel.add(button3);


		
		frame.add(panel, BorderLayout.CENTER);
		
        panel.setBackground(Color.PINK);
        frame.setLocationRelativeTo(null);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Login");
		frame.pack();
		frame.setVisible(true);
		
		
	}
	
	

	
	public static void create_login_popup() {
		//Creates a popup form to login when the button is pressed
        final JFrame parent = new JFrame();
        String pass = "";
         String name = JOptionPane.showInputDialog(parent,
        "Please Enter your Username:", null);
        
         if (name==null) {
        	parent.dispose();
        	         }
        
        
        //if the username given matches no records, try again
        if (! login_info2.containsKey(name)) {
        	while(! login_info2.containsKey(name)) {
        		if(name==null) 
        			break;
        		name = JOptionPane.showInputDialog(parent, 
        		"Username not Found! \nPlease Try Again:", null);
        	}
       }
        //if the user tries to close the login form it will close 
        if(!(name==null)) {
	        pass = JOptionPane.showInputDialog(parent,
	        "Please Enter your Password:", null);
	        if (login_info2.containsKey(name) && login_info2.get(name)[0].equals(pass)) {
	        	logged_in = true;
	        	logged_user=name;
	        	frame.dispose();
	        	panel.setVisible(false);
	        	create_home_popup();
	        	}
        }
        //if the stored password does not equal the input, try again. After 3 attempts the user will be locked out of their acc.
        if(!(name==null)) {
	        if(! login_info2.get(name)[0].equals(pass)) {
	        	int count = 3;
	        	while (! login_info2.get(name)[0].equals(pass) && count>1){
	        		        		pass = JOptionPane.showInputDialog(parent,
	        		 "Incorrect Password! \nPlease Try Again:", null);
	        		count-=1;
	
	        	}
	        	if(count==1) {
	    			JOptionPane.showMessageDialog(parent, 
	    			"Too Many Invalid Attemps. Your Account Has Been Locked for 24 Hours.");
	    		}
	
	        }
        }
        
    }
	
	public static void create_registration_popup() throws IOException {
		//Creates a popup form to register when the button is pressed

		boolean information_valid = true;
        final JFrame parent = new JFrame();
        
        
        String first_name = JOptionPane.showInputDialog(parent,
        		"Please Enter your First Name:", null);
        
        String last_name = JOptionPane.showInputDialog(parent,
        		"Please Enter your Last Name:", null);
        
        String name = JOptionPane.showInputDialog(parent,
        "Please Enter your Desired Username:", null);
        
        //if the username has already been taken, try again
        if(login_info2.containsKey(name)) {
        	while(login_info2.containsKey(name)) {
        		name = JOptionPane.showInputDialog(parent, 
        		"Username Taken! \nPlease Try a New Username:", null);
        	}
       }
        
        
        String pass = JOptionPane.showInputDialog(parent,
        "Please Enter your Password:", null);
        
        String pass2 = JOptionPane.showInputDialog(parent,
        "Please Confirm your Password:", null);      
        
        
        //if the two passwords inputted do not match, try again
        while(! pass.equals(pass2)) {
        	pass2 = JOptionPane.showInputDialog(parent,
        	"Passwords Don't Match \nPlease Try Again:", null);      
        }
        
        String age = JOptionPane.showInputDialog(parent, 
        "Please Enter your Date of Birth:", "mm/dd/yyyy");
        
        ///matches the pattern of the entered date to valid formats
        if (! (Pattern.compile("0[1-9]/[0-3][0-9]/[12][0-9][0-9][0-9]").matcher(age).matches() || 
        					Pattern.compile("1[12]/[0-3][0-9]/[12][0-9][0-9][0-9]").matcher(age).matches())) {
        	while(! (Pattern.compile("0[1-9]/[0-3][0-9]/[12][0-9][0-9][0-9]").matcher(age).matches() || 
        					Pattern.compile("1[12]/[0-3][0-9]/[12][0-9][0-9][0-9]").matcher(age).matches())) {
        		age = JOptionPane.showInputDialog(parent,
        				"Invalid Date Entered.\nPlease Try Again:");
        	}
        }
        
        //if the date entered has the user under the age of 18, they will not be able to use KashKoala
        if(Pattern.compile("../../2.0[5-9]").matcher(age).matches() || Pattern.compile("../../2.1.").matcher(age).matches() || Pattern.compile("../../2.2.").matcher(age).matches()) {
        	JOptionPane.showMessageDialog(parent, "You Must be 18 or Older to use KashKoala.");
        	information_valid=false;
        }
        
        //if the date is okay, the form continues
        if(information_valid==true) {
	        String cash = JOptionPane.showInputDialog(parent,
	        		"Please Enter your Estimated Current Savings:");
	        	
	        int money = Integer.parseInt(cash);
	        //if the user has less than $500, they cannot use KashKoala
	        if (money<500) {
	        	JOptionPane.showMessageDialog(parent, "You Must Have Atleast $500 to use KashKoala.");
	        	information_valid=false;
	        }
        }
        
        //if the info is valid, it will get added to the dictionary and store the user/pass and user/firstname
        
        if (information_valid==true){
        	
        	
           	///testing one list versus a few
        	

        	info_list[0]=pass; //binds username to password
        	info_list[1]=Double.toString(0); //binds username to balance
        	info_list[2]=first_name + " " + last_name; //binds name to username
        	info_list[3]=age; //binds username to age
        	login_info2.put(name, info_list); //adds the list of information to a list of list 
        	
        	write_login_info(login_info2); //writes the updated list to the file
        	
        }

	}
	
	//add friend function
	public static void add_friend() throws IOException {
		JFrame parent = new JFrame();
		String friend_name = JOptionPane.showInputDialog(parent,
        		"Please Enter your Friends Username:", null);
		boolean friend_added = false;
		
		if(friend_name!=null) {
			//if the user inputs their own username, you can't add yourself
			if(friend_name.equals(logged_user)) {
				JOptionPane.showMessageDialog(parent, "You Cannot Add Yourself");
			}

			//if the user currently has no friends, we create a new entry for the pair in the friendships file
			else if(login_info2.containsKey(friend_name)) {
				if(! friendships.containsKey(logged_user)) {
					ArrayList<String> new_friend_list = new ArrayList<String>();
					new_friend_list.add(friend_name);
					friendships.put(logged_user, new_friend_list);
					write_friend_info(friendships);
					//we set friend added to true to not double add any friendships
					friend_added=true;
				}
				//if the user has friends already, we go through their friendships and check if they already have the friend added
				else {
					for(int x=0; x<friendships.get(logged_user).size();x+=1) {
						if(friend_name.equals(friendships.get(logged_user).get(x))) {
							JOptionPane.showMessageDialog(parent, "Friend Already Added!");
							//we set friend added to true because the friendship already exists
							friend_added = true;
						}
					}
				}
				
				//we check if the friend has any friendships as well, and repeat the process from above to add their new friendship
				if(! friendships.containsKey(friend_name)) {
					ArrayList<String> new_friend_list = new ArrayList<String>();
					new_friend_list.add(logged_user);
					friendships.put(friend_name,new_friend_list);
					write_friend_info(friendships);
				}
				else {
					friendships.get(friend_name).add(logged_user);
					write_friend_info(friendships);
				}
				//if the logged user has friends and the new friend is not on the list, the friendship gets added
				if (friend_added==false) {
					friendships.get(logged_user).add(friend_name);
					write_friend_info(friendships);
				}
			}
			
			//if the entered username does not exist, we let the user know
			else {
				JOptionPane.showMessageDialog(parent, "Username not found");
			}
		}

	}
	
	//show the current users friendslist 
	public static void show_friends() {
		JFrame parent = new JFrame();
		String friends = "Friends: ";
		//if the logged user does not exist in the friendship map, it returns no friends
		if(! friendships.containsKey(logged_user)) {
			friends += "None";
		}
		//else, we run through the array list of added friends and display the output
		else { 
			for (int x=0; x<friendships.get(logged_user).size();x+=1) {
				friends += friendships.get(logged_user).get(x) + ", ";
			}
		}

		JOptionPane.showMessageDialog(parent, friends);

	}
	
	public static void send_money() throws IOException {
		JFrame parent = new JFrame();
		String friend_name = JOptionPane.showInputDialog(parent,
        		"Please Enter your Friends Username:", null);
		boolean friend_found = false;
		if(friend_name!=null) {
			
			if(friend_name.equals(logged_user)) {
				JOptionPane.showMessageDialog(parent, "You Cannot Send Money to Yourself");
			}
			else if(login_info2.containsKey(friend_name)) {
				//if either the user or the friend has no friends listed, we can stop
				if(friendships.containsKey(logged_user)==false || friendships.containsKey(friend_name)==false) {
					JOptionPane.showMessageDialog(parent, "You Must Add the User as a Friend First");
				}
				//else we check the logged users friends and make sure the friend has been added
				else {
					for(int x=0; x<friendships.get(logged_user).size(); x+=1) {
						//if the friend is found
						if(friendships.get(logged_user).get(x).equals(friend_name)) {
							//we ask the user how much they would like to send
							String amount_to_send = JOptionPane.showInputDialog(parent, "How Much Would you Like to Send?");
							if(amount_to_send!=null) {
								//if the amount being sent is more than the user has, they receive an error
								if(Double.valueOf(login_info2.get(logged_user)[1])< Double.valueOf(amount_to_send)){
									JOptionPane.showMessageDialog(parent, "Not Enough in your Account");
								}
								//else, we update the balances of the two users
								else {
									login_info2.get(logged_user)[1] = Double.toString(Double.valueOf(login_info2.get(logged_user)[1]) - Double.valueOf(amount_to_send));
									login_info2.get(friend_name)[1] = Double.toString(Double.valueOf(login_info2.get(friend_name)[1]) + Double.valueOf(amount_to_send));
									write_login_info(login_info2);
									JOptionPane.showMessageDialog(parent, "Successfully Sent");
								}
							}
							friend_found=true;
						}
					}
				}
				//if the friend is not found in the users friendslist, they receive an error
				if(friend_found==false) {
					JOptionPane.showMessageDialog(parent, "You Must Add the User as a Friend First");
				}
			}
			else {
				JOptionPane.showMessageDialog(parent, "Friend not Listed");
			}

		}
		
	}
	
	///FUNCTIONS FOR AFFECTING AND CHECKING BALANCE
	
	public static void check_balance() {
		JFrame parent = new JFrame();
		JOptionPane.showMessageDialog(parent, "Current Balance: " + (login_info2.get(logged_user)[1]));

	}
	
	
	public static void add_to_balance() throws IOException {
		String depo = JOptionPane.showInputDialog("How Much Would You Like to Deposit?:");
		if (depo!=null) { //note that the balance is stored as a string, so we must convert it to and from doubles while adding
			login_info2.get(logged_user)[1]=Double.toString(Double.valueOf(login_info2.get(logged_user)[1])+Double.valueOf(depo));
			write_login_info(login_info2);
		}
		}
	public static void remove_from_balance() throws IOException{
		JFrame parent = new JFrame();

		String withd = JOptionPane.showInputDialog("How Much Would You Like to Withdraw?:");
		if(withd!=null) {
			//if the user attempts to withdraw more than they have, they receive an error
			if (Double.valueOf(login_info2.get(logged_user)[1])<Double.valueOf(withd)) {
				JOptionPane.showMessageDialog(parent, "You Cannot Withdraw More than What is in your Account. Please Try Again.");
			}
		
			else {
				//else, the withdraw is successful
				login_info2.get(logged_user)[1]=Double.toString(Double.valueOf(login_info2.get(logged_user)[1])-Double.valueOf(withd));
				JOptionPane.showMessageDialog(parent, "Successfully Withdrawn");
				write_login_info(login_info2);
			}
		}
		
	}
	
	
	
	
	
	
	//FUNCTIONS FOR PORTFOLIO SCREEN
	
	
	//function that checks the price of a given ticker
	public static void check_stock_price() throws IOException{
		JFrame parent = new JFrame();
		String ticker = JOptionPane.showInputDialog("Please Enter the Stock Ticker:");
			if (ticker!=null) {
				//uses YahooFinance api to retrieve stock, and then get the price
				Stock stock = YahooFinance.get(ticker);
		    	BigDecimal price = stock.getQuote().getPrice();
		    	String ticker_name = stock.getName();
		    	if (price==null) {
		    		JOptionPane.showMessageDialog(parent, "The Given Ticker Could Not Be Found");
		    	}
		    	else {
		    		JOptionPane.showMessageDialog(parent, "The Current Price of " + ticker_name + " is " + price);
		    	}
			}
		
	}
	
	//function that allows a user to buy a stock, given a ticker
	public static void buy_stock() throws IOException{
		
		//first we get the current balance of the logged in user
		double curr_bal = Double.valueOf(login_info2.get(logged_user)[1]);

		
		JFrame parent = new JFrame();
		String ticker = JOptionPane.showInputDialog("Please Enter the Stock Ticker you Would Like to Purchase:");
		if(ticker!=null) {
			Stock stock = YahooFinance.get(ticker);
			if(stock==null) {
				JOptionPane.showMessageDialog(parent, "The Given Ticker Could Not Be Found");
			}

			else {
				//we find the current price of the stock and ask the user how many shares they would like to purchase
				BigDecimal price = stock.getQuote().getPrice();
				String ticker_name = stock.getName();
				String shares_to_buy = JOptionPane.showInputDialog(parent, "The Current Price of " + ticker_name + " is " + price + ". You Current Balance is " + curr_bal + ". How Many Shares Would You Like to Buy?");
				
				if (shares_to_buy!=null) {
					int int_shares = Integer.parseInt(shares_to_buy);
					double pay = (int_shares * price.doubleValue());
					//calculate the cost of the entered amount of shares
					//if the user does not have enough in their balance, the purchase does not go through
					if(pay>curr_bal) {
						JOptionPane.showMessageDialog(parent, "You Don't Have Enough Money in your Account!");
					}
					//else, update the current balance of the user and the added shares to their account
					else {
						curr_bal -= pay;
						JOptionPane.showMessageDialog(parent, "Purchase Successful. You Now Have " + curr_bal + " Remaining in your Account");
						login_info2.get(logged_user)[1] = Double.toString(curr_bal);
						write_login_info(login_info2);
						//we create a tuple of the stock,price,and shares owned
						Tuple t = new Tuple(ticker, price, int_shares);
						boolean already_owned = false;

						if(portfolio_info.get(logged_user)==null) {
							ArrayList<Tuple> first_ticker = new ArrayList<Tuple>();
							portfolio_info.put(logged_user,first_ticker);
						}
						else {
							//this for loop checks if the stock is already owned by the customer 
							//if the stock is owned already, update the number of shares and set the price to the average price
							for(int x=0; x<portfolio_info.get(logged_user).size();x+=1) {
								Tuple owned = portfolio_info.get(logged_user).get(x);
								if (t.getTicker().equals(owned.getTicker())) {
									double avgPrice = (t.getShares() * t.getPurchasedPrice().doubleValue() + owned.getShares() * owned.getPurchasedPrice().doubleValue()) / (t.getShares()+owned.getShares());
									owned.setShares(t.getShares());
									owned.setPurchasedPrice(avgPrice);
									already_owned=true;
									write_tickers_owned(portfolio_info);
									
									}
							}
						}
						//if the stock is not already owned, we add it to the users portfolio
						if(already_owned==false)
							portfolio_info.get(logged_user).add(t);
						write_tickers_owned(portfolio_info);
					}
				}
			}
		}
	}
	
	
	public static void sell_stock() throws IOException{
		
		//first we get the current balance of the logged in user
		double curr_bal = Double.valueOf(login_info2.get(logged_user)[1]);

		
		JFrame parent = new JFrame();
		String ticker = JOptionPane.showInputDialog("Please Enter the Stock Ticker you Would Like to Sell:");
		boolean stock_owned = false;
		if(ticker!=null) {
			Stock stock = YahooFinance.get(ticker);
			if (stock==null) {
				JOptionPane.showMessageDialog(parent, "The Given Ticker Could Not Be Found");
				
			}
			else {
				BigDecimal price = stock.getQuote().getPrice();
				String ticker_name = stock.getName();

				for(int x=0; x<portfolio_info.get(logged_user).size();x+=1) {
					//we retrieve each tuple in the users portfolio, to check if they own the one they are attempting to sell
					Tuple to_sell = portfolio_info.get(logged_user).get(x);
					if(to_sell.getTicker().equals(ticker)) {
						//if the desired ticker is found to match
						String shares_to_sell = JOptionPane.showInputDialog(parent, "The Current Price of " + ticker_name + " is " + price + ". You Currently Own " + to_sell.getShares() + " Shares, And Purchased Them at " + to_sell.getPurchasedPrice() + " .How Many Shares Would You Like to Sell?");
						if(shares_to_sell!=null) {
							if(Integer.parseInt(shares_to_sell)>to_sell.getShares()) {
								//if the user tries to sell more shares than they have
								JOptionPane.showMessageDialog(parent, "You do Not Own This Many Shares");
								break;
							}
							else {
								stock_owned=true;
								//calculate the total profit the user will get from selling the desired number of shares
								double total_profit = Integer.parseInt(shares_to_sell) * price.doubleValue();
								JOptionPane.showMessageDialog(parent, "You Have Successfully Sold " + shares_to_sell + " Shares, for a Total of " + total_profit + " . Your New Balance is " + (curr_bal + total_profit) + " .");
								login_info2.get(logged_user)[1] = Double.toString(curr_bal + total_profit); //update the users balance
								write_login_info(login_info2);
								//if they are attempting the sell exactly the same number of shares that they own, we remove the tuple from their portfolio
								if(Integer.parseInt(shares_to_sell)==to_sell.getShares()) {
									portfolio_info.get(logged_user).remove(x);
									write_tickers_owned(portfolio_info);
									break;
								}
								//else we update the number of shares in their portfolio
								else {
									to_sell.sellShares(Integer.parseInt(shares_to_sell));
									write_tickers_owned(portfolio_info);
								}
							}
						}
					}
				}
				if(stock_owned==false) {
					JOptionPane.showMessageDialog(parent, "You Do Not Own This Stock.");
				}
			}
		}
	}

	
	
	
	
	public static String[] print_stocks_owned() throws IOException {
		String portfolio_names = "Company Name: ";
		String portfolio_shares = "Shares Owned: ";
		String portfolio_purchased_at = "Price Purchased At: ";
		String portfolio_current = "Current Price: ";
		String portfolio_change = "Change in Price: ";
		//the different strings allow us to format the text later on the JPanel
		
		for(int x=0; x<portfolio_info.get(logged_user).size();x+=1) {
			Tuple t = portfolio_info.get(logged_user).get(x);
			Stock stock = YahooFinance.get(t.getTicker());
			BigDecimal current_price = stock.getQuote().getPrice();

			
			portfolio_names+=stock.getName() + ", ";
			portfolio_shares+=t.getShares() + ", ";
			portfolio_purchased_at+=t.getPurchasedPrice()+ ", ";
			portfolio_current+=current_price+ ", ";
			portfolio_change+=Double.toString(current_price.doubleValue()-t.getPurchasedPrice().doubleValue())+ ", ";

			
		}
		String[] stocks_data = new String[5];
		stocks_data[0]=portfolio_names;
		stocks_data[1]=portfolio_shares;
		stocks_data[2]=portfolio_purchased_at;
		stocks_data[3]=portfolio_current;
		stocks_data[4]=portfolio_change;
		//we add the strings to a list and return it so it can be easily accessed
		return stocks_data;

		

	}
	
	
	
	public static void create_home_popup() {
		//creates homescreen popup on successful login
		
		
		home = new JPanel();
		//adding the different buttons and setting their on-click actions to respective functions
		
		JButton button = new JButton("Check Balance");
		String first_name = login_info2.get(logged_user)[2];
		first_name=first_name.substring(0, first_name.indexOf(' '));
		JLabel label = new JLabel("Hi " + first_name + "!");
		label.setBorder(new EmptyBorder(0,175,0,0));
				
		button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                check_balance();
            }
        });
		//button for deposit
		JButton button2 = new JButton("Deposit Funds");
		
		button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
					add_to_balance();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
		
		//button for withdraw
		JButton button3 = new JButton("Withdraw Funds");
		
		button3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	try {
					remove_from_balance();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
		
		//button for adding friend
		JButton button5 = new JButton("Add Friend");
		button5.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					add_friend();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//button for viewing friends
		JButton button6 = new JButton("Friendslist");
		button6.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				show_friends();
			}
		});
		
		JButton button8 = new JButton("Send Money");
		button8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					send_money();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});



		//button for logging out. this will take the user back to the login menu
		JButton button4 = new JButton("Logout");
		button4.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				frame.remove(home);
				frame.dispose();
				new backbone();
				}
		});
		//button for viewing the portfolio options
		JButton button7 = new JButton("Portfolio");
		button7.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				frame.remove(home);
				frame.dispose();
				create_portfolio_popup();
				
				}
		});
		
		
		
        home.setBackground(Color.MAGENTA);
		home.setLayout(new GridLayout(0,1));

		home.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		home.add(label);
		home.add(button);
		home.add(button2);
		home.add(button3);
		home.add(button5);
		home.add(button6);
		home.add(button8);
		home.add(button7);
		home.add(button4);
		
		


		
		frame.add(home);
		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Home");
		frame.setSize(400,400);
		frame.setVisible(true);

	}
	
	
	public static void create_portfolio_popup(){
		
		//creating the portfolio home screen as a new JPanel
		portfolio = new JPanel();
		
		
		JButton button = new JButton("Check Stock Price");
		
		//button for checking stock price of a given ticker
		button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
					check_stock_price();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
		//button for logging out. this will take the user back to the login screen
		JButton button2 = new JButton("Logout");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(home);
				frame.dispose();
				new backbone();

			}
		});
		
		//button for buying stock
		JButton button3 = new JButton("Buy Stock");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					buy_stock();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		//button for selling stock
		JButton button5 = new JButton("Sell Stock");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sell_stock();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//button to check portfolio
		JButton button4 = new JButton("Check Stocks Owned");
		button4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					String[] a = print_stocks_owned();
					frame.remove(portfolio);
					frame.dispose();
					create_stocks_popup(a);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		

		
		
		portfolio.setBackground(Color.BLACK);
		portfolio.setLayout(new GridLayout(0,1));

		portfolio.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		
		portfolio.add(button);
		portfolio.add(button3);
		portfolio.add(button5);
		portfolio.add(button4);
		
		portfolio.add(button2);

		
		frame.add(portfolio);
		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Portfolio");
		frame.setSize(400,400);
		///frame.pack();
		frame.setVisible(true);


	}
	
	//creates a new jpanel after the check stocks button is pushed showing currently owned stocks and their prices, shares owned, and price change
	public static void create_stocks_popup(String[] a){
		portfolio = new JPanel();
		
		

	
		JLabel label = new JLabel(logged_user, SwingConstants.CENTER);
		label.setText(String.format("<html><body> %s<br> %s<br> %s<br> %s<br> %s<br></body></html>", a[0],a[1],a[2],a[3],a[4]));

		//button to close this view and return to the portfolio home screen
		JButton button2 = new JButton("Close");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.remove(portfolio);
				frame.dispose();
				create_portfolio_popup();
			}
		});

		
		
		
		portfolio.add(label);
		portfolio.add(button2);
		frame.add(portfolio);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Portfolio");
		frame.setSize(800,800);
		///frame.pack();
		frame.setVisible(true);

	}
	
	//functions for writing and reading user info and friendship info
	public static void write_login_info(Map<String, String[]> login_info2) throws IOException{
		File file = new File("temp");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(login_info2);
        s.close();

	}
	
	public static void write_friend_info(Map<String, ArrayList<String>> friendships) throws IOException{
		File file = new File("friendships");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(friendships);
        s.close();

	}

	public static Map<String,String[]> read_login_info() throws IOException, ClassNotFoundException{
		File file = new File("temp");
	    FileInputStream f = new FileInputStream(file);
	    ObjectInputStream s = new ObjectInputStream(f);
	    Map<String, String[]> fileObj2 = (HashMap<String, String[]>) s.readObject();
	    s.close();

		return fileObj2;
	}

	
	public static Map<String,ArrayList<String>> read_friends() throws IOException, ClassNotFoundException{
		File file = new File("friendships");
	    FileInputStream f = new FileInputStream(file);
	    ObjectInputStream s = new ObjectInputStream(f);
	    Map<String, ArrayList<String>> fileObj3 = (HashMap<String, ArrayList<String>>) s.readObject();
	    s.close();

		return fileObj3;
	}
	
	
	//functions for keeping track of user's portfolios
	public static Map<String,ArrayList<Tuple>> read_tickers_owned() throws IOException, ClassNotFoundException{
		File file = new File("tickers_owned");
	    FileInputStream f = new FileInputStream(file);
	    ObjectInputStream s = new ObjectInputStream(f);
	    Map<String, ArrayList<Tuple>> fileObj3 = (HashMap<String, ArrayList<Tuple>>) s.readObject();
	    s.close();

		return fileObj3;
	}
	
	public static void write_tickers_owned(Map<String, ArrayList<Tuple>> tickers_owned) throws IOException{
		File file = new File("tickers_owned");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(tickers_owned);
        s.close();

	}

	
	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
    	new backbone();
    	login_info2 = read_login_info();
    	friendships = read_friends();
    	portfolio_info = read_tickers_owned();
    	
    		}
	
	
	
}

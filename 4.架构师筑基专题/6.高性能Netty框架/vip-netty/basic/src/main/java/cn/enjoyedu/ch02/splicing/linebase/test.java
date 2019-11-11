package cn.enjoyedu.ch02.splicing.linebase;

public class test {
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i <5; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						new LineBaseEchoClient("127.0.0.1").start();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		Thread.sleep(1000000);
	}

}

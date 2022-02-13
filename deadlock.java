import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class deadlock {

	private Hesap h1 = new Hesap();
	private Hesap h2 = new Hesap();

	private Random random = new Random();

	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();

	public void deadlockCheck(Lock l1, Lock l2) {

		while (true) {

			if (l1.tryLock() == true && l2.tryLock() == true) {
				return;
			} else if (l1.tryLock() == true) {
				l1.unlock();
			} else if (l2.tryLock()) {
				l2.unlock();
			}
		}

	}

	public void thread1Fonk() {

		deadlockCheck(lock1, lock2);

		for (int i = 0; i < 5000; i++) {
			h1.paraTransfer(h1, h2);
		}

		lock1.unlock();
		lock2.unlock();

	}

	public void thread2Fonk() {

		deadlockCheck(lock2, lock1);

		for (int i = 0; i < 5000; i++) {
			h2.paraTransfer(h2, h1);
		}

		lock2.unlock();
		lock1.unlock();

	}

	public void threadOver() {
		System.out.print("hesap1 bakiye: " + h1.getBakiye());
		System.out.println(" hesap2 bakiye: " + h2.getBakiye());
		System.out.println("toplam bakiye: " + (h1.getBakiye() + h2.getBakiye()));
	}

	class Hesap {

		private int bakiye = 10000;

		public void paraYatir(int x) {
			bakiye += x;
		}

		public void paraCek(int x) {
			bakiye -= x;
		}

		public void paraTransfer(Hesap h1, Hesap h2) {
			int miktar = random.nextInt(100);
			h1.paraCek(miktar);
			h2.paraYatir(miktar);
		}

		public int getBakiye() {
			return bakiye;
		}

		public void setBakiye(int bakiye) {
			this.bakiye = bakiye;
		}
	}

	public static void main(String[] args) {

		deadlock dl = new deadlock();

		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				dl.thread1Fonk();
			}
		});
		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				dl.thread2Fonk();
			}
		});

		thread1.start();
		thread2.start();

		try {
			thread1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			thread2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dl.threadOver();

	}

}

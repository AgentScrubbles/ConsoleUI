package console_ui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;


class Animator {
	// private Random rand = new Random(100);
	private final AtomicBoolean animating;
	private final MainWindow window;
	private Collection<Box> boxes;
	private Dimension screen;
	private int maxBoxes;
	private AtomicBoolean clearOrMove;
	private IFinished emptyFinished;
	

	public Animator( MainWindow window, Collection<Box> boxes, Dimension screen, int maxBoxes, AtomicBoolean clearOrMove) {
		this.animating = new AtomicBoolean(false);
		this.window = window;
		this.boxes = boxes;
		this.screen = screen;
		this.maxBoxes = maxBoxes;
		this.clearOrMove = clearOrMove;
		this.emptyFinished = new IFinished(){
			@Override
			public void finish() {
				
			}
		};
	}

	
	public AtomicBoolean animatingLock(){
		return animating;
	}
	
	public void moveBoxes(final int sleepTime, final int xDirChange, final int yDirChange, final int moveAmount, final IFinished whenFinished) {
		new Thread(new Runnable(){

			@Override
			public void run() {

				synchronized (animating) {
					while(animating.get()){
						try {
							animating.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					animating.set(true);
				}

				int amnt = 10;
				for (int i = 0; i < moveAmount; i += amnt) {
					for (Box b : boxes) {
						b.changeCoordinates(xDirChange, yDirChange);
					}
					window.repaint();

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException ingore) {
					}
				}
				// Absolutely terrible, but just in case, make this wait for
				// everything to finish on the event thread
				/**
				 * try { Thread.sleep(sleepTime * boxes.size()); } catch
				 * (InterruptedException e) { block e.printStackTrace(); }
				 **/

				synchronized (animating) {
					animating.set(false);
					animating.notifyAll();
				}
				whenFinished.finish();
			}
			
		}).start();
	}


	public void scrollBoxesLeft() {
		moveBoxes(10, -10, 0, screen.width, new IFinished(){

			@Override
			public void finish() {
				clearOrMove.set(true);  //Screen is to be reset after this move
			}
			
		});
	}

	public void scrollBoxesDown() {
		moveBoxes(10, 0, 10, screen.height, new IFinished(){

			@Override
			public void finish() {
				clearOrMove.set(false);
			}
			
		});
	}

	public void scrollBoxesUp() {
		moveBoxes(10, 0, -10, screen.height, new IFinished(){

			@Override
			public void finish() {
				clearOrMove.set(false);
			}
			
		});
	}

	public void startAutoScroll() {
		Runnable scroller = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Let everything in the UI catch up before we start scrolling
				while (boxes.size() > maxBoxes) {
					// Scroll down

					try {
						scrollBoxesUp();
						Thread.sleep(5000); // Sleep for 5 seconds, then scroll
											// back up
						if (boxes.size() < maxBoxes || animating.get()) {
							return; // Modified while changed
						}
						scrollBoxesDown();
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Repeat until boxes has updated.
				}
			}
		};
		new Thread(scroller).start();
	}

}

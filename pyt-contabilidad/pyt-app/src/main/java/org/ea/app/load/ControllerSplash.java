package org.ea.app.load;

import java.net.URL;
import java.util.ResourceBundle;

import com.pyt.query.interfaces.IVerifyStructuredDB;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ControllerSplash implements Initializable,Runnable{
	    @FXML
	    private ImageView fondo;
	    @FXML
	    private ProgressBar progress;
	    @FXML
	    private TextField percentage;
	    private IVerifyStructuredDB verify;
	    private Boolean runningSplash = true;

	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	       new Thread(this).start();
	    }
	    
	    public final void setVerifySctructureDB(IVerifyStructuredDB verify) {
	    	this.verify = verify;
	    }
	    
	    public final void stop() {
	    	runningSplash = false;
	    }

		@Override
		public void run() {
			while(runningSplash) {
				try {
					Thread.sleep(50);
					Double value = 0.0;
					if(verify != null) {
						value = (double)(verify.counScriptRuns()*100)/verify.countScripts();
						value = value / 100;
					}
					String vlue = (value*100)+" %";
					percentage.setText(vlue);
					progress.setProgress(value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
}
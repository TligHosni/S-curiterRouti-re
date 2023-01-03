package master.pro.houssine.pfe.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

public class Rendez_vousViewModel extends ViewModel {

    MutableLiveData<Date> dateTime = new MutableLiveData(this);
    public void refreshDateTime() {
        // Get the current date and time
//        Date currentDateTime = Calendar.getInstance().getTime();
//        dateTime = currentDateTime;
    }



}

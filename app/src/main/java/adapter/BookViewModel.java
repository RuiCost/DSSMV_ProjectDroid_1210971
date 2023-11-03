package adapter;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
    private MyDAtabaseHelper myDB;

    public LiveData<Boolean> getIsLiked() {
        return isLiked;
    }

    public void toggleIsLiked() {
        isLiked.setValue(!isLiked.getValue());
    }


}

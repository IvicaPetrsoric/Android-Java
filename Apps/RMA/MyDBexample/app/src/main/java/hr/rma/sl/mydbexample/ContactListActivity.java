package hr.rma.sl.mydbexample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.Toast;

/**
 * Created by Marko on 13.5.2016..
 */

public class ContactListActivity extends FragmentActivity implements ContactListFragment.OnContactInteractionListener {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_contacts_list);

        // Provide back to parent activity option:
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setTitle("Phone Contacts");

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.contactsFragment);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.commit();
    }

    @Override
    public void onContactSelected(Uri contactUri) {
        Toast.makeText(ContactListActivity.this, "bubljan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectionCleared() {

    }

}

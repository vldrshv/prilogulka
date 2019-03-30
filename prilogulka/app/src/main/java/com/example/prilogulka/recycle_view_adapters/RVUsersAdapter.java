package com.example.prilogulka.recycle_view_adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prilogulka.R;
import com.example.prilogulka.data.userData.User;

import java.util.List;

public class RVUsersAdapter extends RecyclerView.Adapter<RVUsersAdapter.PersonViewHolder>{
    List<User> persons;

    public RVUsersAdapter(List<User> persons){
        this.persons = persons;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView db_id, db_email, db_password, db_email_check_code, db_is_email_checked;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            db_id = (TextView)itemView.findViewById(R.id.db_id);
            db_email = (TextView)itemView.findViewById(R.id.db_email);
            db_password = (TextView)itemView.findViewById(R.id.db_password);;
            db_email_check_code = (TextView)itemView.findViewById(R.id.db_email_check_code);
            db_is_email_checked = (TextView)itemView.findViewById(R.id.db_is_email_checked);

        }

    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
//        personViewHolder.db_id.setText(persons.get(i).getId()+"");
//        personViewHolder.db_email.setText(persons.get(i).getEmail());
//        personViewHolder.db_password.setText(persons.get(i).getPassword());
//        personViewHolder.db_email_check_code.setText(persons.get(i).getEmailCheckCode());
//        personViewHolder.db_is_email_checked.setText(persons.get(i).getEmailChecked()+"");
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.login_db_presentation, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

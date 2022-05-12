package com.dataHelper;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.common.AlertOrToastMsg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.recyclerViewAdapters.RecyclerViewAdapter;
import com.tutionapp.AddNotes;
import com.tutionapp.AllStudentsList;
import com.tutionapp.NotesDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NotesCRUDOperations implements NotesDetails.GetNotesData, RecyclerViewAdapter.OnStudentListner {

    String ins_id;
    AlertOrToastMsg alertOrToastMsg;
    List<String> notesID = new ArrayList<>();
    Context context;
    List<String> titles = new ArrayList<>();
    List<String> notesDetails = new ArrayList<>();
    ProgressBar progressBar;

    public NotesCRUDOperations(Context context, String ins_id, ProgressBar progressBar){
        this.ins_id = ins_id;
        alertOrToastMsg = new AlertOrToastMsg(context);
        this.progressBar = progressBar;
    }

    public void addNotes(Map<String, Object> map, RecyclerView recyclerView){

        DatabaseReference reference = DatabaseLinks.getDatabaseRef(ins_id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Node.Notes.toString())){
                    titles.removeAll(titles);
                    notesDetails.removeAll(notesDetails);
                    for (DataSnapshot snapshot1 : snapshot.child(Node.Notes.toString()).getChildren()) {
                        String notesIDs = snapshot1.getKey();
                        String id = notesIDs.split("_")[1];
                        notesID.add(id);
                    }
                    Collections.sort(notesID);
                    String lastId = notesID.get(notesID.size()-1);
                    int ids = Integer.parseInt(lastId.split("_")[1])+1;
                    reference.child(Node.Notes.toString()).child("NoteID_"+ids).updateChildren(map, (@Nullable DatabaseError error, @NonNull DatabaseReference ref) -> {
                        if(ref.getKey().equals("NoteID_"+ids)){
                            getNotes(recyclerView);
                            alertOrToastMsg.ToastMsg("Stored Successfully");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    reference.child(Node.Notes.toString()).child("NoteID_1").updateChildren(map, (@Nullable DatabaseError error, @NonNull DatabaseReference ref) -> {
                        getNotes(recyclerView);
                        alertOrToastMsg.ToastMsg("Stored Successfully");
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertOrToastMsg.showAlert("Error", error.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getNotes(RecyclerView recyclerView){
        DatabaseReference reference = DatabaseLinks.getDatabaseRef(ins_id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Node.Notes.toString())){

                    for (DataSnapshot snapshot1 : snapshot.child(Node.Notes.toString()).getChildren()) {
                        String id = snapshot1.getKey();
                        //String id = dataSnapshot.getKey();
                        notesID.add(id);
                        titles.add(snapshot1.child("Title").getValue().toString());
                        notesDetails.add(snapshot1.child("Description").getValue().toString());
                    }
                    AllStudentsList.recyclerView(context, titles, notesDetails, NotesCRUDOperations.this::onClick, recyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    alertOrToastMsg.ToastMsg("No Data...!");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertOrToastMsg.showAlert("Error", error.toString());
                alertOrToastMsg.ToastMsg("No Data...!");
            }
        });
    }

    @Override
    public void getNotesData(String title, String notes) {

    }

    @Override
    public void onClick(int position) {

    }
}

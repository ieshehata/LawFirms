package com.app.lawfirms.controllers;

import androidx.annotation.NonNull;

import com.app.lawfirms.callback.ChatCallback;
import com.app.lawfirms.callback.ConversationCallback;
import com.app.lawfirms.models.ChatModel;
import com.app.lawfirms.models.ConversationModel;
import com.app.lawfirms.models.MessageModel;
import com.app.lawfirms.models.NotificationModel;
import com.app.lawfirms.models.SendNotificationModel;
import com.app.lawfirms.models.UserHeaderModel;
import com.app.lawfirms.utils.SharedData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatController {
    private final String node = "Chats";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://lawfirms-745d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<ChatModel> chats = new ArrayList<>();
    private Query chatQuery;
    private ValueEventListener listener;


    public void save(final ChatModel model, final ChatCallback callback) {
        if (model.getKey() == null || model.getKey().equals("")) {
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        chats.add(model);
                        callback.onSuccess(chats);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e.toString());
                    }
                });
    }

    public void getChat(final String key, final ChatCallback callback) {
        chatQuery = myRef.orderByChild("key").equalTo(key);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel model = snapshot.getValue(ChatModel.class);
                    if (model.getKey().equals(key)) {
                        chats.add(model);
                        for (MessageModel message : model.getMessages()) {
                            if (message.getState() == 0 && !message.getAuthor().getKey().equals(SharedData.currentUser.getKey())) {
                                message.setState(1);
                            }
                        }
                        save(model, new ChatCallback() {


                            @Override
                            public void onSuccess(ArrayList<ChatModel> chats) {

                            }

                            @Override
                            public void onFail(String error) {
                                callback.onFail(error);
                            }
                        });
                    }
                }
                callback.onSuccess(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        };
        chatQuery.addListenerForSingleValueEvent(listener);
    }

    public void getChatAlways(final String key, final ChatCallback callback) {
        chatQuery = myRef.orderByChild("key").equalTo(key);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel model = snapshot.getValue(ChatModel.class);
                    if (model.getKey().equals(key)) {
                        chats.add(model);
                        for (MessageModel message : model.getMessages()) {
                            if (message.getState() == 0 && !message.getAuthor().getKey().equals(SharedData.currentUser.getKey())) {
                                message.setState(1);
                            }
                        }
                        save(model, new ChatCallback() {
                            @Override
                            public void onSuccess(ArrayList<ChatModel> chats) {
                            }

                            @Override
                            public void onFail(String error) {
                                callback.onFail(error);
                            }
                        });
                    }
                }
                callback.onSuccess(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        };
        chatQuery.addValueEventListener(listener);
    }

    public void detachListener() {
        chatQuery.removeEventListener(listener);
    }

    public void delete(ChatModel model, final ChatCallback callback) {
        myRef = database.getReference(node + "/" + model.getKey());
        myRef.removeValue()
                .addOnFailureListener(e -> callback.onFail(e.toString()))
                .addOnSuccessListener(aVoid -> {
                    chats = new ArrayList<>();
                    callback.onSuccess(chats);
                });
    }

    public void newMessage(ConversationModel conversationModel, ChatModel chatModel, String text, String image, UserHeaderModel otherUser, final ChatCallback callback) {
        MessageModel message = new MessageModel();
        message.setNo(chatModel.getMessages().size());
        message.setChatKey(chatModel.getKey());
        message.setAuthor(SharedData.currentUser.toHeader());
        message.setText(text);
        message.setImage(image);
        message.setCreatedAt(Calendar.getInstance().getTime());
        message.setState(0);
        chatModel.getMessages().add(message);

        conversationModel.setLastMessage(image.trim().isEmpty() ? text : "(image)");
        conversationModel.setLastMessageUserKey(SharedData.currentUser.getKey());
        conversationModel.setLastMessageState(0);
        conversationModel.setLastMessageDate(Calendar.getInstance().getTime());

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setTitle(SharedData.currentUser.getName());
        notificationModel.setText(conversationModel.getLastMessage());
        SendNotificationModel newNotificationModel = new SendNotificationModel();
        newNotificationModel.setTo(otherUser.getFcmToken());
        newNotificationModel.setData(notificationModel);

        new APIController().sendNotification(newNotificationModel);
        save(chatModel, new ChatCallback() {
            @Override
            public void onSuccess(ArrayList<ChatModel> chats) {
                new ConversationController().save(conversationModel, new ConversationCallback() {

                    @Override
                    public void onSuccess(ArrayList<ConversationModel> conversations) {
                        callback.onSuccess(new ArrayList<ChatModel>());
                    }

                    @Override
                    public void onFail(String error) {
                        callback.onFail(error);

                    }
                });
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void newRing(ConversationModel conversationModel, ChatModel chatModel, UserHeaderModel otherUser, final ChatCallback callback) {
        MessageModel message = new MessageModel();
        message.setNo(chatModel.getMessages().size());
        message.setChatKey(chatModel.getKey());
        message.setAuthor(SharedData.currentUser.toHeader());
        message.setText("Ring");
        message.setCreatedAt(Calendar.getInstance().getTime());
        message.setState(0);
        chatModel.getMessages().add(message);

        conversationModel.setLastMessage("Ring");
        conversationModel.setLastMessageUserKey(SharedData.currentUser.getKey());
        conversationModel.setLastMessageState(0);
        conversationModel.setLastMessageDate(Calendar.getInstance().getTime());


        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setCall(SharedData.currentUser.getName());
        SendNotificationModel newNotificationModel = new SendNotificationModel();
        newNotificationModel.setTo(otherUser.getFcmToken());
        newNotificationModel.setData(notificationModel);

        new APIController().sendNotification(newNotificationModel);
        save(chatModel, new ChatCallback() {
            @Override
            public void onSuccess(ArrayList<ChatModel> chats) {
                new ConversationController().save(conversationModel, new ConversationCallback() {

                    @Override
                    public void onSuccess(ArrayList<ConversationModel> conversations) {
                        callback.onSuccess(new ArrayList<ChatModel>());
                    }

                    @Override
                    public void onFail(String error) {
                        callback.onFail(error);

                    }
                });
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}

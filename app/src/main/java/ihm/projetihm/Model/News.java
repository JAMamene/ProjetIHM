package ihm.projetihm.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class News implements Parcelable {

    private int id;
    private String title;
    private List<Category> categories;
    private Source source;
    private String author;
    private String image;
    private String content;
    private long date;
    private boolean following;
    private long dateEvent;
    private int eventDuration;
    private boolean registered;

    public News(int id, String title, List<Category> categories, Source source, String author,
                String image, String content, long date, boolean following, long dateEvent, int eventDuration, boolean registered) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.source = source;
        this.author = author;
        this.image = image;
        this.content = content;
        this.date = date;
        this.following = following;
        this.dateEvent = dateEvent;
        this.eventDuration = eventDuration;
        this.registered = registered;
    }

    public long getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isRegistered() {
        return registered;
    }

    public long getDateEvent() {
        return dateEvent;
    }

    public int getEventDuration() {
        return eventDuration;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categories=" + categories +
                ", source=" + source +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", following=" + following +
                ", dateEvent=" + dateEvent +
                ", eventDuration=" + eventDuration +
                ", registered=" + registered +
                '}';
    }

    protected News(Parcel in) {
        id = in.readInt();
        title = in.readString();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<Category>();
            in.readList(categories, Category.class.getClassLoader());
        } else {
            categories = null;
        }
        source = (Source) in.readValue(Source.class.getClassLoader());
        author = in.readString();
        image = in.readString();
        content = in.readString();
        date = in.readLong();
        following = in.readByte() != 0x00;
        dateEvent = in.readLong();
        eventDuration = in.readInt();
        registered = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        dest.writeValue(source);
        dest.writeString(author);
        dest.writeString(image);
        dest.writeString(content);
        dest.writeLong(date);
        dest.writeByte((byte) (following ? 0x01 : 0x00));
        dest.writeLong(dateEvent);
        dest.writeInt(eventDuration);
        dest.writeByte((byte) (registered ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}

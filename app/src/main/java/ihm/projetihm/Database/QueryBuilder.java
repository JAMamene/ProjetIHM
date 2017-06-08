package ihm.projetihm.Database;

import android.util.Log;
import android.widget.CheckBox;

import java.util.Map;

import ihm.projetihm.Model.Category;
import ihm.projetihm.Model.Source;

public class QueryBuilder {

    private String navSelection;
    private String searchSelection;

    public void buildMyStuff() {
        navSelection = "author.following != 0 AND author.following IS NOT NULL ";
    }

    public void buildMyEvents() {
        navSelection = "news.registered != 0 AND news.registered IS NOT NULL ";
    }

    public void setNavNull() {
        navSelection = null;
    }

    public void setNav(Source source) {
        navSelection = "source.name = '" + source.name() + "' ";
    }

    public void setSearch(String value) {
        searchSelection = "news.title LIKE '%" + value + "%' ";
    }

    public String buildRequest(Map<Category, CheckBox> categorySelection) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM news,source,author WHERE news.source = source._id AND news.author = author._id ");
        if (navSelection != null) {
            sb.append("AND ");
            sb.append(navSelection);
        }
        if (searchSelection != null) {
            sb.append("AND ");
            sb.append(searchSelection);
        }
        StringBuilder sb2 = new StringBuilder();
        for (Category c : categorySelection.keySet()) {
            if (categorySelection.get(c).isChecked()) {
                sb2.append('\'').append(c.name()).append("',");
            }
        }
        if (!(sb2.toString().equals(""))) {
            sb2.delete(sb2.length() - 1, sb2.length());
            sb.append("AND ").append("news._id IN " +
                    "(SELECT categ_link.idnews " +
                    "FROM categ_link, category " +
                    "WHERE categ_link.idcateg = category._id AND category.name IN (").append(sb2.toString()).append(")) ");
        }
        sb.append("ORDER BY date DESC");
        Log.d("QUERY", sb.toString());
        return sb.toString();
    }

    public String updateFollow(boolean following, String name) {
        int value = (following) ? 0 : 1;
        String query = "UPDATE author SET following = " + value + " WHERE name = '" + name + "'";
        Log.d("QUERYUPDATE", query);
        return query;
    }

    public String updateRegister(boolean registered, long id) {
        int value = (registered) ? 0 : 1;
        String query = "UPDATE news SET registered = " + value + " WHERE _id = " + id;
        Log.d("QUERYUPDATE", query);
        return query;
    }

    public String buildTweet(String title, long authorId, String image, String content, long date) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO news (title, source, author, image, content, date) VALUES (");
        sb.append("'").append(title.replace('\'', ' ')).append("', ");
        sb.append(Source.TWITTER.getId()).append(", ");
        sb.append(authorId).append(", ");
        sb.append("'").append(image).append("', ");
        sb.append("'").append(content.replace('\'', ' ')).append("', ");
        sb.append(date);
        sb.append(")");
        Log.d("QUERYINSERT", sb.toString());
        return sb.toString();
    }

    public String getCategory(long id) {
        return "SELECT name FROM category, categ_link, news WHERE news._id = " + id + " AND idnews = news._id AND idcateg = category._id";
    }

    public String getAuthor(String name) {
        return "SELECT _id FROM author WHERE name = + '" + name + "' ";
    }
}

package morris.mow.time;

import java.util.ArrayList;
import java.util.List;

public class Group {

  public String name;
  public String date;
  public final List<String> children = new ArrayList<String>();

  public Group(String string, String date) {
    this.name = string;
    this.date=date;
  }

} 

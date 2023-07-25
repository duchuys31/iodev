package vn.iodev.contestmanagementsystem.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "t_files")
@Table(name = "t_files")
public class FileIO {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  private String name;

  private String type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuocThiId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
  private CuocThi cuocThi;

  public CuocThi getCuocThi() {
      return cuocThi;
  }

  public void setCuocThi(CuocThi cuocThi) {
      this.cuocThi = cuocThi;
  }

  @Lob
  @JsonIgnore
  private byte[] data;

  public FileIO() {
  }

  public FileIO(String name, String type, byte[] data) {
      this.name = name;
      this.type = type;
      this.data = data;
  }

  public String getId() {
      return id;
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getType() {
      return type;
  }

  public void setType(String type) {
      this.type = type;
  }

  public byte[] getData() {
      return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}

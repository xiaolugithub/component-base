package pers.silonest.component.http.rest.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import pers.silonest.component.base.courier.Courier;

public class RestResponseBuilder {

  public RestResponseBuilder status(Status status) {
    this.status = status;
    return this;
  }

  public RestResponseBuilder version(String version) {
    this.headerVersion = version;
    return this;
  }

  public RestResponseBuilder date(String date) {
    this.headerDate = date;
    return this;
  }

  public RestResponseBuilder forward(String forward) {
    this.headerForward = forward;
    return this;
  }

  public RestResponseBuilder backward(String backward) {
    this.headerBackward = backward;
    return this;
  }

  public RestResponseBuilder notice(String notice) {
    this.messageNotice.add(notice);
    return this;
  }

  public RestResponseBuilder notices(Collection<? extends String> notices) {
    this.messageNotice.addAll(notices);
    return this;
  }

  public RestResponseBuilder cause(String cause) {
    this.messageCause.add(cause);
    return this;
  }

  public RestResponseBuilder causes(Collection<? extends String> causes) {
    this.messageCause.addAll(causes);
    return this;
  }

  public RestResponseBuilder code(String code) {
    this.messageCode.add(code);
    return this;
  }

  public RestResponseBuilder codes(Collection<? extends String> codes) {
    this.messageCode.addAll(codes);
    return this;
  }

  public RestResponseBuilder content(Object value) {
    this.content = value;
    return this;
  }

  public RestResponseBuilder contents(Map<String, Object> contents) {
    Map<String, Object> content = new HashMap<String, Object>();
    content.putAll(contents);
    this.content = content;
    return this;
  }

  public RestResponseBuilder courier(Courier courier) {
    return courier(courier, null);
  }

  public RestResponseBuilder courier(Courier courier, String contentName) {
    status(courier.getStatus() ? Status.SUCCESS : Status.ERROR);
    if (StringUtils.isNotEmpty(courier.getCause())) {
      cause(courier.getCause());
    }
    if (StringUtils.isNotEmpty(courier.getNotice())) {
      notice(courier.getNotice());
    }
    if (StringUtils.isNotEmpty(courier.getCode())) {
      code(courier.getCode());
    }
    if (courier.getContent() != null) {
      content(courier.getContent());
    }
    return this;
  }

  /**
   * 创建Answer对象.
   *
   * @return 新创建的answer对象
   */
  public RestResponse build() {
    RestResponse restResponse = buildStatus(null);
    restResponse = buildHeader(restResponse);
    restResponse = buildMessage(restResponse);
    restResponse = buildContent(restResponse);
    return restResponse;
  }

  private RestResponse buildStatus(RestResponse restResponse) {
    if (restResponse == null) {
      restResponse = new RestResponse();
    }
    restResponse.setStatus(this.status);
    return restResponse;
  }

  private RestResponse buildHeader(RestResponse restResponse) {
    if (this.headerVersion == null && this.headerDate == null && this.headerForward == null
        && this.headerBackward == null) {
      return restResponse;
    } else {
      Header header = restResponse.getHeader();
      if (header == null) {
        header = new Header();
      }
      header.setVersion(this.headerVersion);
      header.setDate(this.headerDate);
      header.setForward(this.headerForward);
      header.setBackward(this.headerBackward);
      restResponse.setHeader(header);
      return restResponse;
    }
  }

  private RestResponse buildMessage(RestResponse restResponse) {
    if ((this.messageCause == null || this.messageCause.isEmpty())
        && (this.messageNotice == null || this.messageNotice.isEmpty())
        && (this.messageCode == null || this.messageCode.isEmpty())) {
      return restResponse;
    } else {
      Message message = restResponse.getMessage();
      if (message == null) {
        message = new Message();
      }
      if (messageNotice.size() == 0) {
        message.setNotice(null);
      }
      if (messageNotice.size() == 1) {
        message.setNotice(messageNotice.get(0));
      } else {
        StringBuilder sb = new StringBuilder();
        for (String str : this.messageNotice) {
          sb.append(str).append(" ");
        }
        if (sb.length() > 0) {
          sb.deleteCharAt(sb.length() - 1);
        }
        message.setNotice(sb.toString());
      }
      if (messageCause.size() == 0) {
        message.setCause(null);
      } else if (messageCause.size() == 1) {
        message.setCause(messageCause.get(0));
      } else {
        StringBuilder sb = new StringBuilder();
        for (String str : this.messageCause) {
          sb.append(str).append(" ");
        }
        if (sb.length() > 0) {
          sb.deleteCharAt(sb.length() - 1);
        }
        message.setCause(sb.toString());
      }
      if (messageCode.size() == 0) {
        message.setCode(null);
      } else if (messageCode.size() == 1) {
        message.setCode(messageCode.get(0));
      } else {
        StringBuilder sb = new StringBuilder();
        for (String str : this.messageCode) {
          sb.append(str).append(" ");
        }
        if (sb.length() > 0) {
          sb.deleteCharAt(sb.length() - 1);
        }
        message.setCode(sb.toString());
      }
      restResponse.setMessage(message);
      return restResponse;
    }
  }

  private RestResponse buildContent(RestResponse restResponse) {
    if (this.content == null) {
      return restResponse;
    } else {
      restResponse.setContent(this.content);
      return restResponse;
    }
  }

  private Status status;
  private String headerVersion;
  private String headerDate;
  private String headerForward;
  private String headerBackward;
  private List<String> messageNotice = new ArrayList<String>();
  private List<String> messageCause = new ArrayList<String>();
  private List<String> messageCode = new ArrayList<String>();
  private Object content;
}

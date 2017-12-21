package com.ecjia.component.network.base;

/**
 * 接口请求 返回json转换对象最外层的Object,泛型T为有效数据
 * Created by yubaokang on 2016/9/13.
 */
public class BaseRes<T> {
    private BaseStatus status;
    private T data;
    private Paginated paginated;

    public BaseStatus getStatus() {
        return status;
    }

    public void setStatus(BaseStatus status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Paginated getPaginated() {
        return paginated;
    }

    public void setPaginated(Paginated paginated) {
        this.paginated = paginated;
    }

    public class BaseStatus {

        /**
         * succeed : 0
         * error_code : 13
         * error_desc : 不存在的信息
         */

        private int succeed;
        private String error_code;
        private String error_desc;

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }

        public String getError_desc() {
            return error_desc;
        }

        public void setError_desc(String error_desc) {
            this.error_desc = error_desc;
        }
    }

    public class Paginated{
        private int total;
        private int count;
        private int more;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getMore() {
            return more;
        }

        public void setMore(int more) {
            this.more = more;
        }
    }
}

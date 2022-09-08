import { Button, Result } from 'antd'
import React from 'react'
import { useHistory } from 'react-router'

const OrderSuccess = () => {
    const history = useHistory()
    return (
        <Result
            status="success"
            title="Đặt hàng thành công!"
            subTitle="Cảm ơn bạn đã đặt mua hàng của chúng tôi."
            extra={[
                <Button type="primary" key="console" onClick={() => history.push("/")}>
                    Quay lại trang chủ
                </Button>,
                <Button key="buy" onClick={() => history.push("/account/orders")}>Xem đơn hàng</Button>,
            ]}
        />
    )
}

export default OrderSuccess


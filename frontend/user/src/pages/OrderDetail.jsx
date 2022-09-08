import { Descriptions, Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import orderApi from '../api/orderApi';
import numberWithCommas from '../utils/numberWithCommas';

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id'
    },
    {
        title: 'Tên sản phẩm',
        dataIndex: 'book',
        key: 'book',
        render: (book) => book.name
    },
    {
        title: 'Giá tiền',
        dataIndex: 'price',
        key: 'price',
        render: value => numberWithCommas(value) + " đ"
    },
    {
        title: 'Số lượng',
        key: 'quantity',
        dataIndex: 'quantity'
    }
];
const columnStatus = [
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id',
        render: (id, item, index) => index + 1
    },
    {
        title: 'Status',
        dataIndex: 'status',
        key: 'status',
    },
    {
        title: 'Created Time',
        dataIndex: 'createdDateFormat',
        key: 'createdDateFormat',
    },
    {
        title: 'Note',
        dataIndex: 'note',
        key: 'note',
    }
];


const OrderDetail = () => {
    const { id } = useParams()
    console.log(id)
    const [order, setOrder] = useState({})

    useEffect(() => {
        orderApi.get(id).then(res => {
            console.log(res);
            setOrder(res)
        }).catch(err => {
        })

    }, [id])

    return (
        <div>
            <div className="block">
                <div className="block__content">
                    <Table rowKey={"id"} dataSource={order.orderTracks} columns={columnStatus} pagination={false} />
                    <br />
                    <Descriptions title="Thông tin" column={2} size='small' contentStyle={{ fontWeight: 500 }}>
                        <Descriptions.Item label="Fullname">{order.fullName}</Descriptions.Item>
                        <Descriptions.Item label="Phone number">{order.phoneNumber}</Descriptions.Item>
                        <Descriptions.Item label="Address">{order.address}</Descriptions.Item>
                        <Descriptions.Item label="Created time">{order.createdDateFormat}</Descriptions.Item>
                        <Descriptions.Item label="Total Price">{numberWithCommas(order.totalPrice || 0)} đ</Descriptions.Item>
                        <Descriptions.Item label="Status">{order.status}</Descriptions.Item>
                    </Descriptions>
                    <br /><br />
                    <Descriptions title="Sản phẩm" column={1}>
                    </Descriptions>
                    <Table rowKey={"id"} pagination={false} columns={columns} dataSource={order.orderItems} />
                </div>
            </div>
        </div>
    )
}

export default OrderDetail
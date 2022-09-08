import { Button, Descriptions, Form, Input, Modal, notification, Select, Table } from 'antd';
import queryString from 'query-string';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useLocation } from 'react-router-dom';
import orderApi from '../../api/orderApi';

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
    const [form] = Form.useForm()
    const location = useLocation()
    const [order, setOrder] = useState({})

    const handleSubmit = (data) => {
        console.log(data)
        const id = queryString.parse(location.search).id
        orderApi.update(id, data).then(res => {
            console.log(res);
            setOrder(prev => ({
                ...prev,
                status: res.status,
                orderTracks: [...prev.orderTracks, res]
            }))
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });

            form.resetFields()
        })
    }


    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        form.submit()
    }


    useEffect(() => {
        form.resetFields()


        const id = queryString.parse(location.search).id
        orderApi.get(id).then(res => {
            console.log(res);
            setOrder(res)
        }).catch(err => {
        })

    }, [])

    return (
        <div>
            <div className="block">
                <div className="block__content">
                    <Button onClick={showModal} style={{ marginBottom: 5 }}>Update Status</Button>
                    <Table rowKey={"id"} dataSource={order.orderTracks} columns={columnStatus} pagination={false} />
                    <br />
                    <Descriptions title="User Information" column={2} size='small' contentStyle={{ fontWeight: 500 }}>
                        <Descriptions.Item label="Fullname">{order.fullName}</Descriptions.Item>
                        <Descriptions.Item label="Phone number">{order.phoneNumber}</Descriptions.Item>
                        <Descriptions.Item label="Address">{order.address}</Descriptions.Item>
                        <Descriptions.Item label="Created time">{order.createdDateFormat}</Descriptions.Item>
                        <Descriptions.Item label="Total Price">{order.totalPrice} đ</Descriptions.Item>
                        <Descriptions.Item label="Status">{order.status}</Descriptions.Item>
                    </Descriptions>
                    <br /><br />
                    <Descriptions title="Products" column={1}>
                    </Descriptions>
                    <Table rowKey={"id"} pagination={false} columns={columns} dataSource={order.orderItems} />
                </div>
            </div>


            <Modal title="Update Status" visible={isModalVisible}
                onCancel={() => setIsModalVisible(false)}
                onOk={() => handleOk()}
            >
                <Form
                    name="form"
                    form={form}
                    validateTrigger="onSubmit"
                    labelCol={{ flex: '110px' }}
                    labelAlign="left"
                    autoComplete="off"
                    onFinish={handleSubmit}
                    requiredMark={false}
                >
                    <Form.Item
                        label="Status"
                        name="status"
                        rules={[
                            {
                                required: true,
                                message: "Status cannot be empty"
                            }
                        ]}
                    >
                        <Select placeholder="Select status">
                            <Select.Option value="NEW">NEW</Select.Option>
                            <Select.Option value="PENDING">PENDING</Select.Option>
                            <Select.Option value="SHIPPING">SHIPPING</Select.Option>
                            <Select.Option value="DELIVERED">DELIVERED</Select.Option>
                            <Select.Option value="CANCELLED">CANCELLED</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item
                        label="Note"
                        name="note"
                    >
                        <Input placeholder="Note" />
                    </Form.Item>
                </Form>
            </Modal>

        </div>
    )
}

export default OrderDetail
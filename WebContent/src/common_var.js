/*新闻相关的定义变量*/
var oTxtValue_title; //标题
var oTxtValue_key; //关键字
var oTxtValue_cont; //内容
var oTxtValue_comm; //备注
var oTxtValue_from; //新闻来源
var oTxtValue_pred; //预测内容
var oTxtValue_factor; //预测系数
var oTxtValue_reviwc; //复盘重点
var oChkValue_pre; //获取单选框按钮值
var oChkValue_review;//获取复选框按钮值
var oRadioRack;//新闻星级
var newstype;//新闻类型
var oLeague;//联赛
var oTeam;//队伍
var oTxtDate;//时间

var newsId;

var isnewsedit = false;//新闻编辑
var isweiboedit = false;//微博新闻编辑

var delid;//需要删除的新闻id
var editid;//需要编辑的新闻id
var oNewsSum;//需要编辑的新闻id
var oQueryTeamname;//需要查询的新闻参数

var oHtmldiv;//需要删除的新闻元素

/*复盘相关的定义变量*/

var isreviewedit = false;//复盘编辑

var oReview_title ; //标题
var oReview_zhuchang ;//主让/客让
var oReview_chushi ;//初始盘口
var oReview_zuizhong ;//终盘
var oReview_zouxiang ;//盘口走向
var oReview_zongjie ;//总结
var oReview_cont ; //内容
var oReview_comm ; //备注
var oReview_date ;//时间
var oReview_path ; //网页路径

var oReview_rshengfu;// 让球方胜平负
var oReview_pshengfu;// 盘口赢走输
var oReview_bifen;// 比分（含半场）
var oReview_bshengfu;// 半全场胜负
var oReview_daxiao;// 大小球以2.5为例

var oReview_delid;//需要删除的复盘id
var oReview_editid;//需要编辑的复盘id
var oReview_Sum;//需要编辑的复盘id
var oReview_Querytitle;//需要查询的复盘参数
var oReview_Queryzhuchang;//需要查询的主让/客让
var oReview_Querychushi;//需要查询的初始盘口
var oReview_Queryzuizhong;//需要查询的终盘
var oReview_Queryzouxiang;//需要查询的盘口走向

var oTxtValue_picid;

var reviewId;//选中修改记录的数据库ID
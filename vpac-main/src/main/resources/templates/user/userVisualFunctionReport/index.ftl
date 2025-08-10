<#include "${_ROOT_PATH_}/layout/pagelayout.ftl"/>
<@headContent/>
<@bodyContent>
<div class="panel panel-default">
  <div class="panel-body table-content">
    <form class="table-search">
      <div class="form-inline">
        <div class="form-group">
          <input type="text" placeholder="创建时间" name="createTime" class="form-control layer-date" title="创建时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="修改时间" name="updateTime" class="form-control layer-date" title="修改时间">
        </div>
        <div class="form-group">
          <input type="text" placeholder="用户档案ID" name="userArchiveId" class="form-control" title="用户档案ID">
        </div>
        <div class="form-group">
          <input type="text" placeholder="检查日期" name="inspectDate" class="form-control layer-date" title="检查日期">
        </div>
        <div class="form-group">
          <input type="text" placeholder="主视眼" name="dominantEye" class="form-control" title="主视眼">
        </div>
        <div class="form-group">
          <input type="text" placeholder="遮盖实验" name="coverTest" class="form-control" title="遮盖实验">
        </div>
        <button type="button" class="btn btn-info table-search-submit" title="查询"><i class="fa fa-search"></i></button>
        <button type="button" class="btn btn-white table-search-reset" title="清空条件"><i class="fa fa-recycle"></i>
        </button>
      </div>
    </form>
      <div class="table-toolbar">
        <button onclick="userVisualFunctionReport.edit(true)" type="button" class="btn btn-white">
          <i class="fa fa-list-alt"></i>
          查看
        </button>
        <button onclick="userVisualFunctionReport.add()" type="button" class="btn btn-info">
          <i class="fa fa-plus"></i>
          新增
        </button>
        <button onclick="userVisualFunctionReport.edit()" type="button" class="btn btn-warning">
          <i class="fa fa-edit"></i>
          修改
        </button>
        <button onclick="userVisualFunctionReport.del()" type="button" class="btn btn-danger">
          <i class="fa fa-remove"></i>
          删除
        </button>
      </div>
    <table id="userVisualFunctionReport_table_list" class="table-striped"></table>
  </div>
</div>
<div class="modal" id="userVisualFunctionReport_modal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
              class="sr-only">关闭</span>
        </button>
        <h4 class="modal-title">
        </h4>
      </div>
      <div class="modal-body">
        <form class="panel-body form-horizontal">
          <input type="hidden" class="form-control" name="id"/>
          <div class="form-group">
            <label class="control-label">创建时间：</label>
            <div>
              <input type="text" class="form-control layer-date" name="createTime" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">修改时间：</label>
            <div>
              <input type="text" class="form-control layer-date" name="updateTime" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">用户档案ID：</label>
            <div>
              <input type="text" class="form-control" name="userArchiveId" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">检查日期：</label>
            <div>
              <input type="text" class="form-control layer-date" name="inspectDate" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">主视眼：</label>
            <div>
              <input type="text" class="form-control" name="dominantEye" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">遮盖实验：</label>
            <div>
              <input type="text" class="form-control" name="coverTest" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">诉求：</label>
            <div>
              <input type="text" class="form-control" name="appeal" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">左眼球镜：</label>
            <div>
              <input type="text" class="form-control" name="leftSphere" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">右眼球镜：</label>
            <div>
              <input type="text" class="form-control" name="rightSphere" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">左眼柱镜：</label>
            <div>
              <input type="text" class="form-control" name="leftCylinder" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">右眼柱镜：</label>
            <div>
              <input type="text" class="form-control" name="rightCylinder" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">左眼轴位：</label>
            <div>
              <input type="text" class="form-control" name="leftAxial" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">右眼轴位：</label>
            <div>
              <input type="text" class="form-control" name="rightAxial" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">左眼最佳矫正视力：</label>
            <div>
              <input type="text" class="form-control" name="leftBcva" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">右眼最佳矫正视力：</label>
            <div>
              <input type="text" class="form-control" name="rightBcva" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">左眼针孔视力：</label>
            <div>
              <input type="text" class="form-control" name="leftPhva" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">右眼针孔视力：</label>
            <div>
              <input type="text" class="form-control" name="rightPhva" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近附加：</label>
            <div>
              <input type="text" class="form-control" name="adds" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">远重影：</label>
            <div>
              <input type="text" class="form-control" name="farGhost" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近重影：</label>
            <div>
              <input type="text" class="form-control" name="nearGhost" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">视远不清：</label>
            <div>
              <input type="text" class="form-control" name="farClearly" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">视近不清：</label>
            <div>
              <input type="text" class="form-control" name="nearClearly" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">眼痛：</label>
            <div>
              <input type="text" class="form-control" name="ophthalmalgia" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">头痛：</label>
            <div>
              <input type="text" class="form-control" name="headache" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">串字跳行：</label>
            <div>
              <input type="text" class="form-control" name="skipLines" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">疲劳：</label>
            <div>
              <input type="text" class="form-control" name="fatigue" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">其它症状：</label>
            <div>
              <input type="text" class="form-control" name="otherSymptoms" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">立体视：</label>
            <div>
              <input type="text" class="form-control" name="stereopsis" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">worth 4 dot：</label>
            <div>
              <input type="text" class="form-control" name="worth4Dot" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">远距水平隐斜：</label>
            <div>
              <input type="text" class="form-control" name="dlp" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">远距垂直隐斜：</label>
            <div>
              <input type="text" class="form-control" name="dvp" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近距水平隐斜：</label>
            <div>
              <input type="text" class="form-control" name="nlp" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近距垂直隐斜：</label>
            <div>
              <input type="text" class="form-control" name="nvp" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">AC/A：</label>
            <div>
              <input type="text" class="form-control" name="acA" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">调节反应：</label>
            <div>
              <input type="text" class="form-control" name="bcc" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">负相对调节：</label>
            <div>
              <input type="text" class="form-control" name="nra" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">正相对调节：</label>
            <div>
              <input type="text" class="form-control" name="pra" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">调节灵敏度：</label>
            <div>
              <input type="text" class="form-control" name="af" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">调节幅度：</label>
            <div>
              <input type="text" class="form-control" name="amp" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">集合近点：</label>
            <div>
              <input type="text" class="form-control" name="npc" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">远融像范围BI：</label>
            <div>
              <input type="text" class="form-control" name="farFusionRangeBi" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">远融像范围BO：</label>
            <div>
              <input type="text" class="form-control" name="farFusionRangeBo" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近融像范围BI：</label>
            <div>
              <input type="text" class="form-control" name="nearFusionRangeBi" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">近融像范围BO：</label>
            <div>
              <input type="text" class="form-control" name="nearFusionRangeBo" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">备注：</label>
            <div>
              <input type="text" class="form-control" name="remark" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">缓解眼镜：</label>
            <div>
              <input type="text" class="form-control" name="reliefGlasses" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">训练内容：</label>
            <div>
              <input type="text" class="form-control" name="trainingContent" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">训练方式：</label>
            <div>
              <input type="text" class="form-control" name="trainingStyle" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">家庭训练器械：</label>
            <div>
              <input type="text" class="form-control" name="homeTrainingEquipment" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">复查时间：</label>
            <div>
              <input type="text" class="form-control" name="reviewTime" required/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label">视光师：</label>
            <div>
              <input type="text" class="form-control" name="optometrists" required/>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary save-btn" onclick="userVisualFunctionReport.save()"><i class="fa fa-save"></i>提交</button>
        <button type="button" class="btn btn-white" data-dismiss="modal"><i class="fa fa-close"></i>关闭</button>
      </div>
    </div>
  </div>
</div>
<script src="${ctx!}/admin/pub/js/user/userVisualFunctionReport/index.js"></script>

</@bodyContent>